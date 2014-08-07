package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LookFromAboveMessage extends AbstractUserMessage {

	@JsonCreator
	public LookFromAboveMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}