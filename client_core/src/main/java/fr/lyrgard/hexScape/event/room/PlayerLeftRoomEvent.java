package fr.lyrgard.hexScape.event.room;

import fr.lyrgard.hexScape.model.player.Player;

public class PlayerLeftRoomEvent {

	private Player player;
	
	private String roomId;

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
}
