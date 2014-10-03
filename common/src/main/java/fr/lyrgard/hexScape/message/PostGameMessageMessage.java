package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostGameMessageMessage extends AbstractPlayerMessage {

	private String message;
	
	private String gameId;
	
	@JsonCreator
	public PostGameMessageMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("message") String message, 
			@JsonProperty("gameId") String gameId) {
		super(playerId);
		this.message = message;
		this.gameId = gameId;
	}

	public String getMessage() {
		return message;
	}

	public String getGameId() {
		return gameId;
	}
	
	
}
