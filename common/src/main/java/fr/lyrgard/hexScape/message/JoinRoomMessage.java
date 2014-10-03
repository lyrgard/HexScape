package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinRoomMessage extends AbstractMessage {

	private String roomId;

	@JsonCreator
	public JoinRoomMessage(@JsonProperty("roomId") String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return roomId;
	}
	
	
}
