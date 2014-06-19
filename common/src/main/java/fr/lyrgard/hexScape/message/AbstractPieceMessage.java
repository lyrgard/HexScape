package fr.lyrgard.hexScape.message;

public abstract class AbstractPieceMessage extends AbstractUserMessage {

	private String cardInstanceId;
	
	private String pieceId;

	
	public AbstractPieceMessage(String playerId, String cardInstanceId, String pieceId) {
		super(playerId);
		this.cardInstanceId = cardInstanceId;
		this.pieceId = pieceId;
	}


	public String getPieceId() {
		return pieceId;
	}


	public String getCardInstanceId() {
		return cardInstanceId;
	}
}
