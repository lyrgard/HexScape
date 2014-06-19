package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlacePieceMessage extends AbstractPieceMessage {

	private String pieceModelId;
	
	@JsonCreator
	public PlacePieceMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("cardInstanceId") String cardInstanceId,
			@JsonProperty("pieceModelId") String pieceModelId) {
		super(playerId, cardInstanceId, null);
		this.pieceModelId = pieceModelId;
	}

	public String getPieceModelId() {
		return pieceModelId;
	}

}
