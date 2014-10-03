package fr.lyrgard.hexScape.listener;

import java.util.Iterator;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.LeaveGameMessage;
import fr.lyrgard.hexScape.message.RestoreGameMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.IdGenerator;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.service.MarkerService;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

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
	
	@Subscribe public void onRestoreGame(RestoreGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			Game game = message.getGame();
			String gameId = IdGenerator.getInstance().getNewGameId();
			
			game.setId(gameId);
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(CurrentUserInfo.getInstance().getId(), game);
			CoreMessageBus.post(resultMessage);
		}
	}
	
	@Subscribe public void onCreateGame(CreateGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String name = message.getName();
			Map map = message.getMap();
			int playerNumber = message.getPlayerNumber();
			String gameId = "1";
			
			Game game = new Game();
			game.setId(gameId);
			game.setName(name);
			game.setMap(map);
			game.setPlayerNumber(playerNumber);
			for (int i = 1; i <= playerNumber; i++) {
				Player player = new Player();
				player.setId(Integer.toString(i));
				player.setName("Player " + i);
				player.setColor(ColorEnum.values()[(i-1) % ColorEnum.values().length]);
				game.getPlayers().add(player);
			}
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(CurrentUserInfo.getInstance().getId(), game);
			CoreMessageBus.post(resultMessage);
		}
	}
	
	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		Game game = message.getGame();
		
		MarkerService.getInstance().normalizeMarkers(game);
		Universe.getInstance().getGamesByGameIds().put(game.getId(), game);
		GuiMessageBus.post(message);
		
	}
	
	@Subscribe public void onStartGame(StartGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String userId = message.getUserId();
			String gameId = message.getGameId();
			GameStartedMessage resultMessage = new GameStartedMessage(userId, gameId);
			CoreMessageBus.post(resultMessage);
		}
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		String gameId = message.getGameId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null) {
			game.setStarted(true);
			GuiMessageBus.post(message);
		}
	}
	
	@Subscribe public void onGameEnded(GameEndedMessage message) {
		String gameId = message.getGameId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null) {
			GuiMessageBus.post(message);
			
			Room room = CurrentUserInfo.getInstance().getRoom();
			if (room != null) {
				Iterator<Game> it = room.getGames().iterator();
				while (it.hasNext()) {
					Game roomGame = it.next();
					if (roomGame.getId().equals(gameId)) {
						it.remove();
						break;
					}
				}
			}
			Universe.getInstance().getGamesByGameIds().remove(gameId);
		}
	}
	
	@Subscribe public void onJoinGame(JoinGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String userId = message.getUserId();
			String gameId = message.getGameId();
			String playerId = message.getPlayerId();
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			
			if (game != null) {
				CoreMessageBus.post(new GameJoinedMessage(userId, game, playerId));
			}
			
		}
		
	}
	
	@Subscribe public void onGameJoined(GameJoinedMessage message) {
		String userId = message.getUserId();
		Game game = message.getGame();
		String playerId = message.getPlayerId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		
		if (game != null && user != null) {

			
			Player player = game.getPlayer(playerId);
			if (player != null) {
				player.setUserId(user.getId());
				MarkerService.getInstance().normalizeMarkers(game);
				user.setGame(game);
				user.setPlayer(player);
				if (CurrentUserInfo.getInstance().getId().equals(userId)) {
					CurrentUserInfo.getInstance().setGame(game);
					CurrentUserInfo.getInstance().setPlayer(player);
					
				}
				Universe.getInstance().getGamesByGameIds().put(game.getId(), game);
				GuiMessageBus.post(message);

				if (HexScapeCore.getInstance().isOnline()) {
					if (CurrentUserInfo.getInstance().getId().equals(userId)) {
						if (game.isStarted()) {
							GuiMessageBus.post(new GameStartedMessage(userId, game.getId()));
						}
					}
				}
			}
		}
	}
	
	@Subscribe public void onLeaveGame(LeaveGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			User user = CurrentUserInfo.getInstance();
			GameLeftMessage resultMessage = new GameLeftMessage(user.getPlayerId(), user.getGameId());
			CoreMessageBus.post(resultMessage);
			
			GameEndedMessage resultMessage2 = new GameEndedMessage(user.getGameId());
			CoreMessageBus.post(resultMessage2);
		}
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			Player player = game.getPlayer(playerId);

			if (player != null && player.getUserId() != null) {
				User user = Universe.getInstance().getUsersByIds().get(player.getUserId());

				user.setGame(null);
				user.setPlayer(null);
				player.setUserId(null);

				GuiMessageBus.post(message);
			}
		}
	}
}
