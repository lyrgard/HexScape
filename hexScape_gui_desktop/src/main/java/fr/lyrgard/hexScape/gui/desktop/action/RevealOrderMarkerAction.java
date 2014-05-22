package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;

public class RevealOrderMarkerAction extends AbstractAction {

	private static final long serialVersionUID = -4915127447162778036L;

	private Card card;
	
	private RevealableMarkerInstance marker;

	public RevealOrderMarkerAction(Card card, RevealableMarkerInstance marker) {
		super("Reveal this order marker");
		this.card = card;
		this.marker = marker;
	}

	public void actionPerformed(ActionEvent e) {
		HexScapeCore.getInstance().getMarkerService().revealMarkerOnCard(card, marker);		
	}
	
	
}
