package fr.lyrgard.hexScape.message;

public class PiecePlacedMessage extends AbstractPieceMessage {

	public PiecePlacedMessage(String playerId, String gameId, String pieceId) {
		super(playerId, gameId, pieceId);
	}
	
}
