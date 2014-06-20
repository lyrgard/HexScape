package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovePieceMessage extends AbstractPieceMessage {

	@JsonCreator
	public MovePieceMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("pieceId") String pieceId) {
		super(playerId, pieceId);
	}

}
