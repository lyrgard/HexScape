package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LookFreelyMessage extends AbstractPlayerMessage {

	@JsonCreator
	public LookFreelyMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}
}
