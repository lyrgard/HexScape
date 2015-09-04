package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomMessagePostedMessage extends AbstractMessage {

	private String message;
	
	private String roomId;
	
	@JsonCreator
	public RoomMessagePostedMessage(
			@JsonProperty("userId") String userId,
			@JsonProperty("message") String message,
			@JsonProperty("roomId") String roomId) {
		this.message = message;
		this.roomId = roomId;
	}

	public String getMessage() {
		return message;
	}

	public String getRoomId() {
		return roomId;
	}
}
