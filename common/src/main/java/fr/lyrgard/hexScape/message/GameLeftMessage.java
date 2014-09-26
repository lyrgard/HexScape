package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameLeftMessage extends AbstractPlayerMessage {
	
	private String gameId;

	@JsonCreator
	public GameLeftMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId) {
		super(playerId);
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}
	
}
