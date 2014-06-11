package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
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
			game.setMap(map);
			game.setPlayerNumber(playerNumber);
			game.getPlayersIds().add(player.getId());
			player.setGame(game);
			Universe.getInstance().getGamesByGameIds().put(gameId, game);
			
			GameCreatedMessage resultMessage = new GameCreatedMessage(playerId, gameId, name, map.getName(), playerNumber, playerNumber - game.getPlayersIds().size());
			ServerNetwork.getInstance().sendMessageToRoom(resultMessage, player.getRoom().getId());
		}
	}
}
