package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LookingFreelyMessage extends AbstractPlayerMessage {

	@JsonCreator
	public LookingFreelyMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}