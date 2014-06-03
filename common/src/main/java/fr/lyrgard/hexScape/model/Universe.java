package fr.lyrgard.hexScape.model;

import java.util.HashMap;
import java.util.Map;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

public class Universe {

	private static final Universe INSTANCE = new Universe();

	public static Universe getInstance() {
		return INSTANCE;
	}
	
	private Universe() {
		
	}
	
	private Map<String, Player> playersByIds = new HashMap<>();
	
	private Map<String, Room> roomsByRoomIds = new HashMap<>();
	
	private Map<String, Game> gamesByGameIds = new HashMap<>();

	public Map<String, Player> getPlayersByIds() {
		return playersByIds;
	}

	public Map<String, Room> getRoomsByRoomIds() {
		return roomsByRoomIds;
	}

	public Map<String, Game> getGamesByGameIds() {
		return gamesByGameIds;
	}
	
	
}
