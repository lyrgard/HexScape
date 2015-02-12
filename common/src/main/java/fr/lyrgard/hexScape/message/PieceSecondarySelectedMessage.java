package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PieceSecondarySelectedMessage extends AbstractPieceMessage {

	String primarySelectedPieceId;

	@JsonCreator
	public PieceSecondarySelectedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("pieceId") String pieceId,
			@JsonProperty("primarySelectedPieceId") String primarySelectedPieceId) {
		super(playerId, pieceId);
		this.primarySelectedPieceId = primarySelectedPieceId;
	}

	public String getPrimarySelectedPieceId() {
		return primarySelectedPieceId;
	}
}
