package fr.lyrgard.hexScape.server;

import java.util.HashMap;
import java.util.Map;

import fr.lyrgard.hexScape.model.ServerConstant;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.server.listener.DiceMessageListener;
import fr.lyrgard.hexScape.server.listener.GameMessageListener;
import fr.lyrgard.hexScape.server.listener.MarkerMessageListener;
import fr.lyrgard.hexScape.server.listener.PieceMessageListener;
import fr.lyrgard.hexScape.server.listener.RoomMessageListener;
import fr.lyrgard.hexScape.server.listener.ServerMessageListener;
import fr.lyrgard.hexScape.server.service.IdService;
import fr.lyrgard.hexScape.server.service.RoomService;
import fr.lyrgard.hexscape.server.network.ServerNetwork;

public class HexScapeServer {

	private static final HexScapeServer instance = new HexScapeServer();
	
	private Map<String, Room> rooms = new HashMap<>();
	
	private Map<String, Game> games = new HashMap<>();
	
	private Map<String, Player> players = new HashMap<>();
	
	private RoomService roomService = new RoomService();
	
	private IdService idService = new IdService();
	
	public static HexScapeServer getInstance() {
		return instance;
	}
	
	private HexScapeServer() {
		Room room = new Room();
		room.setId(Room.DEFAULT_ROOM_ID);
		room.setName(Room.DEFAULT_ROOM_ID);
		Universe.getInstance().getRoomsByRoomIds().put(Room.DEFAULT_ROOM_ID, room);
		
		ServerMessageListener.start();
		RoomMessageListener.start();
		GameMessageListener.start();
		DiceMessageListener.start();
		PieceMessageListener.start();
		MarkerMessageListener.start();
		try {
			ServerNetwork.getInstance().start(ServerConstant.SERVER_PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
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
}
