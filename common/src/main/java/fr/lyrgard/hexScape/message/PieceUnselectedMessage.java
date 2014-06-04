package fr.lyrgard.hexScape.message;

public class PieceUnselectedMessage extends AbstractPieceMessage {

	public PieceUnselectedMessage(String playerId, String gameId,
			String cardInstanceId, String pieceId) {
		super(playerId, gameId, cardInstanceId, pieceId);
	}


}
