package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage extends AbstractUserMessage {

	private String message;
	
	@JsonCreator
	public ErrorMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("message") String message) {
		super(playerId);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
