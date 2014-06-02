package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.card.Card;

public class RemoveAllMarkersFromCardAction extends AbstractAction {
	
	private static final long serialVersionUID = 2139602334359226593L;

	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/remove.png"));
	
	private Card card;
	
	public RemoveAllMarkersFromCardAction(Card card) {
		super("Remove all markers from this card", icon);
		this.card = card;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		HexScapeCore.getInstance().getMarkerService().removeAllMarkersFromCard(card);
	}

}
