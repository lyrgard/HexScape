package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StartGameMessage extends AbstractUserMessage {
	private String gameId;

	@JsonCreator
	public StartGameMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("gameId") String gameId) {
		super(userId);
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}
	
}
