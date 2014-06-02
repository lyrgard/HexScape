package fr.lyrgard.hexScape.event.room;

import fr.lyrgard.hexScape.model.player.Player;

public class RoomMessagePostedEvent {
	
	private Player player;
	
	private String roomId;
	
	private String message;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
