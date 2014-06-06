package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PieceUnselectedMessage extends AbstractPieceMessage {

	@JsonCreator
	public PieceUnselectedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId,
			@JsonProperty("cardInstanceId") String cardInstanceId, 
			@JsonProperty("pieceId") String pieceId) {
		super(playerId, gameId, cardInstanceId, pieceId);
	}


}
