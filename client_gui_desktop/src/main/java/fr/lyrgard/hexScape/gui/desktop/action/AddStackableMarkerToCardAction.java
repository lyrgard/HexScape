package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;

public class AddStackableMarkerToCardAction extends AbstractAction {

	private static final long serialVersionUID = 7209605070483212248L;

	private MarkerDefinition marker;
	
	private CardInstance card;
	
	private int number;
	
	public AddStackableMarkerToCardAction(MarkerDefinition marker, CardInstance card, int number) {
		super();
		String name = null;
		if (number < 0 ) {
			name = Integer.toString(number);
		} else {
			name = "+" + number;
		}
		putValue(Action.NAME, name);
		this.marker = marker;
		this.card = card;
		this.number = number;
	}

	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		String gameId = HexScapeCore.getInstance().getGameId();
		
		PlaceMarkerMessage message = new PlaceMarkerMessage(playerId, gameId, card.getId(), marker.getId(), number);
		CoreMessageBus.post(message);
	}

}
