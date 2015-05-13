package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.RevealMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;

public class RevealMarkerAction extends AbstractAction {

	private static final long serialVersionUID = -4915127447162778036L;

	private CardInstance card;
	
	private HiddenMarkerInstance marker;

	public RevealMarkerAction(CardInstance card, HiddenMarkerInstance marker) {
		super("Reveal this order marker");
		this.card = card;
		this.marker = marker;
	}

	public void actionPerformed(ActionEvent e) {
		
		RevealMarkerMessage message = new RevealMarkerMessage(card.getId(), marker.getId());
		CoreMessageBus.post(message);		
	}
	
	
}
