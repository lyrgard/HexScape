package fr.lyrgard.hexScape.service;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

public class RoomService {

private static final RoomService INSTANCE = new RoomService();
	
	public static RoomService getInstance() {
		return INSTANCE;
	}
	
	private RoomService() {
	}
	
	public void joinRoom(Room room) {
		Universe.getInstance().getRoomsByRoomIds().put(room.getId(), room);
		HexScapeCore.getInstance().setRoomId(room.getId());
		
		for (Player player : room.getPlayers()) {
			Universe.getInstance().getPlayersByIds().put(player.getId(), player);
		}
	}
	
	public void playerJoinedRoom(Player player) {
		String roomId = HexScapeCore.getInstance().getRoomId();
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (room != null) {
			Universe.getInstance().getPlayersByIds().put(player.getId(), player);
			room.getPlayers().add(player);
		}
	}
}
