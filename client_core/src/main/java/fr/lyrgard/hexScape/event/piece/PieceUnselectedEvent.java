package fr.lyrgard.hexScape.event.piece;

import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.player.Player;

public class PieceUnselectedEvent extends PieceEvent {

	public PieceUnselectedEvent(MoveablePiece piece, Player actingUser) {
		super(piece, actingUser);
	}

}
