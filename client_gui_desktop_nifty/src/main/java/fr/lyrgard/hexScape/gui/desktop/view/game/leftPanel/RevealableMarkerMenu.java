package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.gui.desktop.action.RevealMarkerAction;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;

public class RevealableMarkerMenu extends JPopupMenu {

	private static final long serialVersionUID = 2503273763767899214L;

	public RevealableMarkerMenu(CardInstance card, HiddenMarkerInstance marker) {
		JMenuItem revealMarkerItem = new JMenuItem(new RevealMarkerAction(card, marker));
		add(revealMarkerItem);
	}
}
