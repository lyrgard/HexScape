package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.card.Card;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;

public class AddMarkerToCardAction extends AbstractAction {

	private static final long serialVersionUID = -5302251867512312354L;

	private MarkerDefinition marker;
	
	private Card card;
	
	
	
	public AddMarkerToCardAction(MarkerDefinition marker, Card card) {
		super("Add a " + marker.getName() + " to this card");
		this.marker = marker;
		this.card = card;
		final ImageIcon icon = new ImageIcon(marker.getImage().getAbsolutePath());
		putValue(Action.SMALL_ICON, icon);
		
	}

	public void actionPerformed(ActionEvent e) {
		HexScapeCore.getInstance().getMarkerService().addMarkerToCard(card, marker.getId());
	}

}
