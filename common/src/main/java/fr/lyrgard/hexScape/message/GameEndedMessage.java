package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameEndedMessage extends AbstractMessage {

	private String gameId;

	@JsonCreator
	public GameEndedMessage(@JsonProperty("gameId") String gameId) {
		super();
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}
}
