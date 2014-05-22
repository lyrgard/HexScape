package fr.lyrgard.hexScape.gui.desktop.components.menuComponent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.gui.desktop.action.RevealOrderMarkerAction;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;

public class RevealableMarkerMenu extends JPopupMenu {

	private static final long serialVersionUID = 2503273763767899214L;

	public RevealableMarkerMenu(Card card, RevealableMarkerInstance marker) {
		JMenuItem revealMarkerItem = new JMenuItem(new RevealOrderMarkerAction(card, marker));
		add(revealMarkerItem);
	}
}
