package fr.lyrgard.hexScape.event.piece;

import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.player.Player;

public class PieceAddedEvent extends PieceEvent {

	public PieceAddedEvent(MoveablePiece piece, Player actingUser) {
		super(piece, actingUser);
	}

	
}
