package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PiecePlacedMessage extends AbstractPieceMessage {

	private String modelId;
	
	@JsonCreator
	public PiecePlacedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardInstanceId") String cardInstanceId, 
			@JsonProperty("pieceId") String pieceId, 
			@JsonProperty("modelId") String modelId) {
		super(playerId, gameId, cardInstanceId, pieceId);
		this.modelId = modelId;
	}

	public String getModelId() {
		return modelId;
	}
	
}
