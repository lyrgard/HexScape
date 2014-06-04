package fr.lyrgard.hexScape.message;

public class PieceMovedMessage extends AbstractPieceMessage {

	public PieceMovedMessage(String playerId, String gameId,
			String cardInstanceId, String pieceId) {
		super(playerId, gameId, cardInstanceId, pieceId);
	}



}
