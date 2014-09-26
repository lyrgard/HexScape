package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameMessagePostedMessage extends AbstractMessage {

	private String playerId;
	
	private String message;
	
	@JsonCreator
	public GameMessagePostedMessage(
			@JsonProperty("playerId") String playerId,
			@JsonProperty("message") String message) {
		this.playerId = playerId;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getPlayerId() {
		return playerId;
	}
}
