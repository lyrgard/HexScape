package fr.lyrgard.hexScape.message;

public class PieceRemovedMessage extends AbstractPieceMessage {

	public PieceRemovedMessage(String playerId, String gameId,
			String cardInstanceId, String pieceId) {
		super(playerId, gameId, cardInstanceId, pieceId);
	}



}
