package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameLeftMessage extends AbstractPlayerMessage {
	
	private String gameId;
	
	private String userId;

	@JsonCreator
	public GameLeftMessage(
			@JsonProperty("userId") String userId,
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId) {
		super(playerId);
		this.userId = userId;
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public String getUserId() {
		return userId;
	}
	
}
