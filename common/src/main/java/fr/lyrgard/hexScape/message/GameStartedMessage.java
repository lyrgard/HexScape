package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStartedMessage extends AbstractUserMessage {
	private String gameId;

	@JsonCreator
	public GameStartedMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("gameId") String gameId) {
		super(userId);
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}
	
}
