package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;

public class AddStackableMarkerToCardAction extends AbstractAction {

	private static final long serialVersionUID = 7209605070483212248L;

	private MarkerDefinition markerType;
	
	private CardInstance card;
	
	private int number;
	
	public AddStackableMarkerToCardAction(MarkerDefinition markerType, CardInstance card, int number) {
		super();
		String name = null;
		if (number < 0 ) {
			name = Integer.toString(number);
		} else {
			name = "+" + number;
		}
		putValue(Action.NAME, name);
		this.markerType = markerType;
		this.card = card;
		this.number = number;
	}

	public void actionPerformed(ActionEvent e) {
		
		PlaceMarkerMessage message = new PlaceMarkerMessage(card.getId(), markerType.getId(), number, null, true);
		CoreMessageBus.post(message);
	}

}
