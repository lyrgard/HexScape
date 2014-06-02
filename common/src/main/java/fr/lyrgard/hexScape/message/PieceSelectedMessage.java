package fr.lyrgard.hexScape.message;

public class PieceSelectedMessage extends AbstractPieceMessage {

	public PieceSelectedMessage(String playerId, String gameId, String pieceId) {
		super(playerId, gameId, pieceId);
	}

}
