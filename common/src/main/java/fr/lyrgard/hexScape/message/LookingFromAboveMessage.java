package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LookingFromAboveMessage extends AbstractPlayerMessage {

	@JsonCreator
	public LookingFromAboveMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}