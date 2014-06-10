package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinRoomMessage extends AbstractUserMessage {

	private String roomId;

	@JsonCreator
	public JoinRoomMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("roomId") String roomId) {
		super(playerId);
		this.roomId = roomId;
	}

	public String getRoomId() {
		return roomId;
	}
	
	
}
