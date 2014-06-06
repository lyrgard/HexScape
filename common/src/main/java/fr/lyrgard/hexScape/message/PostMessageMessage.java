package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostMessageMessage extends AbstractUserMessage {

	private String message;
	
	private String roomId;
	
	private String gameId;
	
	@JsonCreator
	public PostMessageMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("message") String message, 
			@JsonProperty("roomId") String roomId,
			@JsonProperty("gameId") String gameId) {
		super(playerId);
		this.message = message;
		this.roomId = roomId;
		this.gameId = gameId;
	}

	public String getMessage() {
		return message;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getGameId() {
		return gameId;
	}
	
	
}
