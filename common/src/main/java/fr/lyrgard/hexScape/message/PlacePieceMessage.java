package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlacePieceMessage extends AbstractPieceMessage {

	private String pieceModelId;
	
	private String cardInstanceId;
	
	@JsonCreator
	public PlacePieceMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("cardInstanceId") String cardInstanceId,
			@JsonProperty("pieceModelId") String pieceModelId) {
		super(playerId, null);
		
		this.pieceModelId = pieceModelId;
		this.cardInstanceId = cardInstanceId;
	}

	public String getPieceModelId() {
		return pieceModelId;
	}

	public String getCardInstanceId() {
		return cardInstanceId;
	}

}
