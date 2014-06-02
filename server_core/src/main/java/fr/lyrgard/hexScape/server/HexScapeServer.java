package fr.lyrgard.hexScape.server;

import java.util.HashMap;
import java.util.Map;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.server.service.IdService;
import fr.lyrgard.hexScape.server.service.RoomService;

public class HexScapeServer {

	private static final HexScapeServer instance = new HexScapeServer();
	
	private HexScapeServerJme3Application app;
	
	private Map<String, Room> rooms = new HashMap<>();
	
	private Map<String, Game> games = new HashMap<>();
	
	private Map<String, Player> players = new HashMap<>();
	
	private Map<String, HostedConnection> connectionByPlayerIds = new HashMap<>();
	
	private RoomService roomService = new RoomService();
	
	private IdService idService = new IdService();
	
	public static HexScapeServer getInstance() {
		return instance;
	}
	
	private HexScapeServer() {
		Room room = new Room();
		room.setName(Room.DEFAULT_ROOM_ID);
		rooms.put(Room.DEFAULT_ROOM_ID, room);
		app = new HexScapeServerJme3Application();
		app.start();
	}
	
	public static void main(String... args) {
	}

	public Map<String, Room> getRooms() {
		return rooms;
	}

	public Map<String, Game> getGames() {
		return games;
	}

	public Map<String, Player> getPlayers() {
		return players;
	}

	public RoomService getRoomService() {
		return roomService;
	}

	public IdService getIdService() {
		return idService;
	}
	
	public Server getServer() {
		return app.getServer();
	}

	public Map<String, HostedConnection> getConnectionByPlayerIds() {
		return connectionByPlayerIds;
	}
}
