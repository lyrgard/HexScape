package fr.lyrgard.hexScape.message;

public abstract class AbstractPieceMessage extends AbstractGameMessage {

	private String pieceId;

	
	public AbstractPieceMessage(String playerId, String gameId, String pieceId) {
		super(playerId, gameId);
		this.pieceId = pieceId;
	}


	public String getPieceId() {
		return pieceId;
	}
}
