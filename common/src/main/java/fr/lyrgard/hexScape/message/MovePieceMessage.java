package fr.lyrgard.hexScape.message;

public class MovePieceMessage extends AbstractPieceMessage {

	public MovePieceMessage(String playerId, String gameId,
			String cardInstanceId, String pieceId) {
		super(playerId, gameId, cardInstanceId, pieceId);
	}

}
