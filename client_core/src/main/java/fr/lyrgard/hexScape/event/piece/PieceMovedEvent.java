package fr.lyrgard.hexScape.event.piece;

import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.player.Player;

public class PieceMovedEvent extends PieceEvent {

	public PieceMovedEvent(MoveablePiece piece, Player actingUser) {
		super(piece, actingUser);
	}

}
