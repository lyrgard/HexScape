package fr.lyrgard.hexScape.message;

public class PlacePieceMessage extends AbstractPieceMessage {

	private String pieceModelId;
	
	public PlacePieceMessage(String playerId, String gameId, String cardInstanceId,String pieceModelId) {
		super(playerId, gameId, cardInstanceId, null);
		this.pieceModelId = pieceModelId;
	}

	public String getPieceModelId() {
		return pieceModelId;
	}

}
