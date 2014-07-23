package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.LeaveGameMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexscape.server.network.ServerNetwork;
import fr.lyrgard.hexscape.server.network.id.IdGenerator;

public class GameMessageListener {

	private static GameMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new GameMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private GameMessageListener() {
	}
	
	
	@Subscribe public void onCreateGame(CreateGameMessage message) {
		String playerId = message.getPlayerId();
		String name = message.getName();
		Map map = message.getMap();
		int playerNumber = message.getPlayerNumber();
		
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null && player.getRoom() != null && player.getRoom().getId() != null) {
			String gameId = IdGenerator.getInstance().getNewGameId();
			Game game = new Game();
			game.setId(gameId);
			game.setName(name);
			game.setMap(map);
			game.setPlayerNumber(playerNumber);
			game.getPlayersIds().add(player.getId());
			player.setGameId(gameId);
			Universe.getInstance().getGamesByGameIds().put(gameId, game);
			player.getRoom().getGames().add(game);
			
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(playerId, game);
			ServerNetwork.getInstance().sendMessageToRoom(resultMessage, player.getRoom().getId());
		}
	}
	
	@Subscribe public void onStartGame(StartGameMessage message) {
		String playerId = message.getPlayerId();
		String gameId = message.getGameId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (player != null && player.getRoom() != null && player.getRoom().getId() != null && game != null && game.getPlayersIds().contains(playerId)) {
			if (!game.isStarted()) {
				game.setStarted(true);
				GameStartedMessage resultMessage = new GameStartedMessage(playerId, gameId);
				ServerNetwork.getInstance().sendMessageToRoom(resultMessage, player.getRoom().getId());
			} else {
				ErrorMessage resultMessage = new ErrorMessage(playerId, "Unable to start the game : the game has already started");
				ServerNetwork.getInstance().sendMessageToPlayer(resultMessage, playerId);
			}
		}
	}
	
	@Subscribe public void onJoinGame(JoinGameMessage message) {
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (player != null && game != null) {
			if (player.getGameId() == null && !game.getPlayersIds().contains(playerId) && game.getPlayerNumber() > game.getPlayersIds().size()) {
				if (!game.isStarted()) {
					player.setGameId(game.getId());
					game.getPlayersIds().add(playerId);
					GameJoinedMessage resultMessage = new GameJoinedMessage(playerId, gameId); 
					
					ServerNetwork.getInstance().sendMessageToRoom(resultMessage, player.getRoom().getId());
				} else {
					ErrorMessage resultMessage = new ErrorMessage(playerId, "Unable to join the game : the game has already started");
					ServerNetwork.getInstance().sendMessageToPlayer(resultMessage, playerId);
				}
			}
		}
	}
	
	@Subscribe public void onLeaveGame(LeaveGameMessage message) {
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (player != null && game != null) {
			player.setGameId(null);
			game.getPlayersIds().remove(player.getId());
			
			GameLeftMessage resultMessage = new GameLeftMessage(playerId, gameId);
			ServerNetwork.getInstance().sendMessageToRoom(resultMessage, player.getRoom().getId());
			
			if (game.getPlayersIds().isEmpty()) {
				player.getRoom().getGames().remove(game);
				Universe.getInstance().getGamesByGameIds().remove(game.getId());
				GameEndedMessage returnMessage3 = new GameEndedMessage(game.getId());
				ServerNetwork.getInstance().sendMessageToRoom(returnMessage3, player.getRoom().getId());
			}
		}
	}
	
	@Subscribe public void onMessagePosted(MessagePostedMessage message) {
		String gameId = message.getGameId();
		
		if (gameId != null) {
			ServerNetwork.getInstance().sendMessageToGame(message, gameId);
		}
	}
	
	@Subscribe public void onArmyLoaded(ArmyLoadedMessage message) {
		String playerId = message.getPlayerId();
		//Army army = message.getArmy();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null && player.getGameId() != null) {
			ServerNetwork.getInstance().sendMessageToGame(message, player.getGameId());
		}
	}
	
}
