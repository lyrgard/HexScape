package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LookFromSelectedPieceMessage extends AbstractPlayerMessage {

	@JsonCreator
	public LookFromSelectedPieceMessage (
			@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}