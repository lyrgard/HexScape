package fr.lyrgard.hexScape.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

public class Universe {

	private static final Universe INSTANCE = new Universe();

	public static Universe getInstance() {
		return INSTANCE;
	}
	
	private Universe() {
		
	}
	
	private Map<String, Player> playersByIds = new ConcurrentHashMap<>();
	
	private Map<String, Room> roomsByRoomIds = new ConcurrentHashMap<>();
	
	private Map<String, Game> gamesByGameIds = new ConcurrentHashMap<>();
	
	private Map<String, PieceInstance> piecesByPieceIds = new ConcurrentHashMap<>();

	public Map<String, Player> getPlayersByIds() {
		return playersByIds;
	}

	public Map<String, Room> getRoomsByRoomIds() {
		return roomsByRoomIds;
	}

	public Map<String, Game> getGamesByGameIds() {
		return gamesByGameIds;
	}

	public Map<String, PieceInstance> getPiecesByPieceIds() {
		return piecesByPieceIds;
	}
	
	
}
