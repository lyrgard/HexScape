package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameMessagePostedMessage extends AbstractMessage {

	private String userId;
	
	private String message;
	
	@JsonCreator
	public GameMessagePostedMessage(
			@JsonProperty("userId") String userId,
			@JsonProperty("message") String message) {
		this.userId = userId;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getUserId() {
		return userId;
	}

	
}
