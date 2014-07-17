package fr.lyrgard.hexScape.service;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class RoomService {

	private static final RoomService INSTANCE = new RoomService();
	
	public static RoomService getInstance() {
		return INSTANCE;
	}
	
	private RoomService() {
	}
	

	
	public void createdGame(String gameId, String name, int playerNumber) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		
		Game game = new Game();
		game.setId(gameId);
		game.setName(name);
		game.setPlayerNumber(playerNumber);
		game.setMap(HexScapeCore.getInstance().getHexScapeJme3Application().getScene().getMap());
		game.getPlayersIds().add(playerId);
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			
		}
		
		Universe.getInstance().getGamesByGameIds().put(gameId, game);
	}
}
