package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.Player;
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
		}
		
	}
}
