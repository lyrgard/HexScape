package fr.lyrgard.hexScape.event.piece;

import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.Player;

public class PieceRemovedEvent extends PieceEvent {

	public PieceRemovedEvent(MoveablePiece piece, Player actingUser) {
		super(piece, actingUser);
	}

}
