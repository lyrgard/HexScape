package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.model.card.CardInstance;

public class ArmyCardMenuFactory implements PopupMenuFatcory {

	final CardInstance card;
	
	public ArmyCardMenuFactory(final CardInstance card) {
		this.card = card;
	}
	
	@Override
	public JPopupMenu getMenu() {
		return new ArmyCardMenu(card);
	}

}
