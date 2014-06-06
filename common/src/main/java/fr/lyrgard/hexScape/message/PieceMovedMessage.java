package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PieceMovedMessage extends AbstractPieceMessage {

	@JsonCreator
	public PieceMovedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId,
			@JsonProperty("cardInstanceId") String cardInstanceId, 
			@JsonProperty("pieceId") String pieceId) {
		super(playerId, gameId, cardInstanceId, pieceId);
	}



}
