package fr.lyrgard.hexScape.message;

public class PiecePlacedMessage extends AbstractPieceMessage {

	private String modelId;
	
	public PiecePlacedMessage(String playerId, String gameId, String cardInstanceId, String pieceId, String modelId) {
		super(playerId, gameId, cardInstanceId, pieceId);
		this.modelId = modelId;
	}

	public String getModelId() {
		return modelId;
	}
	
}
