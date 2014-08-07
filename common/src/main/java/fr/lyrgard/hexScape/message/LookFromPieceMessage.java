package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LookFromPieceMessage extends AbstractPieceMessage {

	@JsonCreator
	public LookFromPieceMessage (
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("playerId") String pieceId) {
		super(playerId, pieceId);
	}

}