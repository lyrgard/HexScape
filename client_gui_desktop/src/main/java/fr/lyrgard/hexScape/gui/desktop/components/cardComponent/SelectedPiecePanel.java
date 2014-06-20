package fr.lyrgard.hexScape.gui.desktop.components.cardComponent;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.MovePieceAction;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;

public class SelectedPiecePanel extends JPanel {
	private static final long serialVersionUID = 7100885729187357599L;

	public SelectedPiecePanel() {
		GuiMessageBus.register(this);
	}

	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {

		final String pieceId = message.getPieceId();
		String playerId = message.getPlayerId();

		if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					SelectedPiecePanel.this.removeAll();
					add(new JButton(new MovePieceAction(pieceId)));
					validate();
					repaint();
					//SwingUtilities.getWindowAncestor(SelectedPiecePanel.this).pack();
				}
			});
		}
	}

	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		String playerId = message.getPlayerId();

		if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					SelectedPiecePanel.this.removeAll();
					validate();
					repaint();
					//SwingUtilities.getWindowAncestor(SelectedPiecePanel.this).pack();
				}
			});
		}
	}

	@Subscribe public void onPieceRemoved(PieceRemovedMessage message) {
		String playerId = message.getPlayerId();

		if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					SelectedPiecePanel.this.removeAll();
					validate();
					repaint();
					//SwingUtilities.getWindowAncestor(SelectedPiecePanel.this).pack();
				}
			});
		}
	}
}
