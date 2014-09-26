package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinGameMessage extends AbstractUserMessage {

	private String gameId;
	
	private String playerId;

	@JsonCreator
	public JoinGameMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("gameId") String gameId,
			@JsonProperty("playerId") String playerId) {
		super(userId);
		this.gameId = gameId;
		this.playerId = playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public String getPlayerId() {
		return playerId;
	}


	
}
