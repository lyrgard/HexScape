package fr.lyrgard.hexScape.message;

public abstract class AbstractPieceMessage extends AbstractUserMessage {

	private String pieceId;

	
	public AbstractPieceMessage(String playerId, String pieceId) {
		super(playerId);
		this.pieceId = pieceId;
	}


	public String getPieceId() {
		return pieceId;
	}
}
