package fr.lyrgard.hexScape.message;

public abstract class AbstractPieceMessage extends AbstractGameMessage {

	private String cardInstanceId;
	
	private String pieceId;

	
	public AbstractPieceMessage(String playerId, String gameId, String cardInstanceId, String pieceId) {
		super(playerId, gameId);
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
