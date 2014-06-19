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
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
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
	
	@Subscribe public void onCreateGame(CreateGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String playerId = message.getPlayerId();
			String name = message.getName();
			Map map = message.getMap();
			int playerNumber = message.getPlayerNumber();
			String gameId = "1";
			
			Game game = new Game();
			game.setId(gameId);
			game.setName(name);
			game.setMap(map);
			game.setPlayerNumber(playerNumber);
			game.getPlayersIds().add(playerId);
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(playerId, game);
			CoreMessageBus.post(resultMessage);
		}
	}
	
	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		String playerId = message.getPlayerId();
		Game game = message.getGame();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			Universe.getInstance().getGamesByGameIds().put(game.getId(), game);
			player.setGame(game);
			GuiMessageBus.post(message);
		}
		
	}
	
	@Subscribe public void onStartGame(StartGameMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String playerId = message.getPlayerId();
			String gameId = message.getGameId();
			GameStartedMessage resultMessage = new GameStartedMessage(playerId, gameId);
			CoreMessageBus.post(resultMessage);
		}
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onGameEnded(GameEndedMessage message) {
		String gameId = message.getGameId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null) {
			GuiMessageBus.post(message);
			
			for (String playerId : game.getPlayersIds()) {
				Player player = Universe.getInstance().getPlayersByIds().get(playerId);
				if (player != null) {
					player.setGame(null);
				}
			}
			String roomId = HexScapeCore.getInstance().getRoomId();
			Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
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
		ClientNetwork.getInstance().send(message);
	}
	
	@Subscribe public void onGameJoined(GameJoinedMessage message) {
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (player != null && game != null) {
			player.setGame(game);
			game.getPlayersIds().add(playerId);
			
			GuiMessageBus.post(message);
		}
	}
}
