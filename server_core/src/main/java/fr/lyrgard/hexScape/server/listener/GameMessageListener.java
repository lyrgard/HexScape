package fr.lyrgard.hexScape.server.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameMessagePostedMessage;
import fr.lyrgard.hexScape.message.GameObservedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.LeaveGameMessage;
import fr.lyrgard.hexScape.message.ObserveGameMessage;
import fr.lyrgard.hexScape.message.RestoreGameMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.IdGenerator;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.server.service.GameService;
import fr.lyrgard.hexscape.server.network.ServerNetwork;

public class GameMessageListener {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(GameMessageListener.class);

	private static GameMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new GameMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private GameMessageListener() {
	}
	
	@Subscribe public void onRestoreGame(RestoreGameMessage message) {
		Game game = message.getGame();
		String userId = message.getSessionUserId();
		String gameId = IdGenerator.getInstance().getNewGameId();
		
		game.setId(gameId);
		game.setStarted(false);
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null) {
			Universe.getInstance().getGamesByGameIds().put(gameId, game);
			user.getRoom().getGames().add(game);
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(user.getId(), game);
			ServerNetwork.getInstance().sendMessageToRoom(resultMessage, user.getRoom().getId());
		}
	}
	
	@Subscribe public void onCreateGame(CreateGameMessage message) {
		String userId = message.getSessionUserId();
		String name = message.getName();
		Map map = message.getMap();
		int playerNumber = message.getPlayerNumber();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getRoom() != null) {
			String gameId = IdGenerator.getInstance().getNewGameId();
			Game game = new Game();
			game.setId(gameId);
			game.setName(name);
			game.setMap(map);
			game.setPlayerNumber(playerNumber);
			for (int i = 1; i <= playerNumber; i++) {
				Player player = new Player();
				player.setId(IdGenerator.getInstance().getNewPlayerId());
				player.setName("PLAYER " + i);
				player.setColor(ColorEnum.values()[(i-1) % ColorEnum.values().length]);
				game.getPlayers().add(player);
			}
			Universe.getInstance().getGamesByGameIds().put(gameId, game);
			user.getRoom().getGames().add(game);
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(user.getId(), game);
			ServerNetwork.getInstance().sendMessageToRoom(resultMessage, user.getRoom().getId());
		}
	}
	
	@Subscribe public void onStartGame(StartGameMessage message) {
		String userId = message.getSessionUserId();
		String gameId = message.getGameId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null) {
			Game game = user.getGame();

			if (user != null && user.getRoom() != null && game != null) {
				if (!game.isStarted()) {
					game.setStarted(true);
					GameStartedMessage resultMessage = new GameStartedMessage(user.getId(), gameId);
					ServerNetwork.getInstance().sendMessageToRoom(resultMessage, user.getRoom().getId());
				} else {
					ErrorMessage resultMessage = new ErrorMessage(user.getId(), "Unable to start the game : the game has already started");
					ServerNetwork.getInstance().sendMessageToUser(resultMessage, user.getId());
				}
			}
		}
	}
	
	@Subscribe public void onObserveGame(ObserveGameMessage message) {
		String userId = message.getSessionUserId();
		String gameId = message.getGameId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (user != null && game != null && user.getRoom() != null ) {
			if (game.isStarted()) {
				user.setGame(game);
				game.getObserversIds().add(userId);
				GameObservedMessage resultMessage = new GameObservedMessage(user.getId(), gameId);
				ServerNetwork.getInstance().sendMessageToRoom(resultMessage, user.getRoom().getId());
			} else {
				ErrorMessage resultMessage = new ErrorMessage(user.getId(), "Unable to observe the game : the game has not started yet");
				ServerNetwork.getInstance().sendMessageToUser(resultMessage, user.getId());
			}
		}
	}
	
	@Subscribe public void onJoinGame(JoinGameMessage message) {
		String userId = message.getSessionUserId();
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);

		if (user != null && game != null) {
			Player player = game.getPlayer(playerId);

			if (user.getPlayer() == null && player.getUserId() == null) {
				player.setUserId(user.getId());
				user.setPlayer(player);
				user.setGame(game);
				
				try {
					for (User userInRoom : user.getRoom().getUsers()) {
						Game clone = Game.fromJson(game.toJson());
						
						GameService.removeUnseeableHiddenMarkersInfos(clone, userInRoom.getId());
						
						GameJoinedMessage resultMessage = new GameJoinedMessage(user.getId(), clone, player.getId()); 

						ServerNetwork.getInstance().sendMessageToUser(resultMessage, userInRoom.getId());
					}
				} catch (IOException e) {
					LOGGER.error("Error reading map data from json", e);
				}

				
			} else {
				if (user.getPlayer() != null) {
					ErrorMessage resultMessage = new ErrorMessage(playerId, "Unable to join the game : you already joined another game");
					ServerNetwork.getInstance().sendMessageToUser(resultMessage, userId);
				} else if (player.getUserId() != null) {
					ErrorMessage resultMessage = new ErrorMessage(playerId, "Unable to join the game as player " + player.getName() + " : another user already took this player");
					ServerNetwork.getInstance().sendMessageToUser(resultMessage, userId);
				}
			}
		} 
	}
	
	@Subscribe public void onLeaveGame(LeaveGameMessage message) {
		String userId = message.getSessionUserId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null) {
			Game game = user.getGame();

			if (game != null) {
				
				String playerId = null;
				if (user.getPlayer() != null && game.getPlayers().contains(user.getPlayer())) {
					Player player = user.getPlayer();
					playerId = player.getId();
					player.setUserId(null);
					user.setPlayer(null);
					
				} else if (game.getObserversIds().contains(userId)) {
					game.getObserversIds().remove(userId);
				}
				user.setGame(null);

				GameLeftMessage resultMessage = new GameLeftMessage(user.getId(), playerId, game.getId());
				ServerNetwork.getInstance().sendMessageToRoom(resultMessage, user.getRoom().getId());

				if (game.getFreePlayers().size() == game.getPlayers().size()) {
					user.getRoom().getGames().remove(game);
					Universe.getInstance().getGamesByGameIds().remove(game.getId());

					GameEndedMessage returnMessage2 = new GameEndedMessage(game.getId());
					ServerNetwork.getInstance().sendMessageToRoom(returnMessage2, user.getRoom().getId());
				}
			}
		}
	}
	
	@Subscribe public void onMessagePosted(GameMessagePostedMessage message) {
		String userId = message.getSessionUserId();
		String messageContent = message.getMessage();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGameId() != null) {
			ServerNetwork.getInstance().sendMessageToGame(new GameMessagePostedMessage(userId, messageContent), user.getGameId());
		}
	}
	
	@Subscribe public void onArmyLoaded(ArmyLoadedMessage message) {
		String userId = message.getSessionUserId();
		String playerId = message.getPlayerId();
		Army army = message.getArmy();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
	
		if (user != null && user.getGameId() != null) {
			Player owner = user.getGame().getPlayer(playerId);
			if (owner != null) {
				owner.setArmy(army);
				ServerNetwork.getInstance().sendMessageToGame(message, user.getGameId());
			}
		}
	}
	
}
