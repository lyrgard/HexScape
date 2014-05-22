package fr.lyrgard.hexScape.gui.desktop.card;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.piece.PieceRemovedEvent;
import fr.lyrgard.hexScape.event.piece.PieceSelectedEvent;
import fr.lyrgard.hexScape.event.piece.PieceUnselectedEvent;
import fr.lyrgard.hexScape.gui.desktop.action.MoveSelectedPieceAction;

public class SelectedPiecePanel extends JPanel {
private static final long serialVersionUID = 7100885729187357599L;
	
	public SelectedPiecePanel() {
		HexScapeCore.getInstance().getEventBus().register(this);
	}

	@Subscribe public void pieceSelected(PieceSelectedEvent event) {
		this.removeAll();
		add(new JButton(new MoveSelectedPieceAction()));
		validate();
		repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}

	@Subscribe public void pieceUnselected(PieceUnselectedEvent event) {
		this.removeAll();
		validate();
		repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}

	@Subscribe public void pieceRemoved(PieceRemovedEvent event) {
		this.removeAll();
		validate();
		repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}
}
