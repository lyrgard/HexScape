package fr.lyrgard.hexScape.event.piece;

import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.player.Player;

public abstract class PieceEvent {

	private MoveablePiece piece;
	
	private Player actingUser;

	public PieceEvent(MoveablePiece piece, Player actingUser) {
		super();
		this.piece = piece;
		this.actingUser = actingUser;
	}



	public MoveablePiece getPiece() {
		return piece;
	}



	public Player getActingUser() {
		return actingUser;
	}
	
	
}
