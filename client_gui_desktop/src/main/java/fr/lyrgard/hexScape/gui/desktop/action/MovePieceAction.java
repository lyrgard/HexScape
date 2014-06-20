package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.MovePieceMessage;
import fr.lyrgard.hexScape.model.piece.PieceInstance;

public class MovePieceAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(MovePieceAction.class.getResource("/gui/icons/move_figure.png"));

	private String pieceId;
	
	public MovePieceAction(PieceInstance piece) {
		this(piece.getId());
	}

	public MovePieceAction(String pieceId) {
		super("move", icon);
		this.pieceId = pieceId;
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		
		MovePieceMessage message = new MovePieceMessage(playerId, pieceId);
		CoreMessageBus.post(message);
		
	}
}
