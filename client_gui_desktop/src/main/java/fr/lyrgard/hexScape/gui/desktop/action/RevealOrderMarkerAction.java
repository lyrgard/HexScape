package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.RevealMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;

public class RevealOrderMarkerAction extends AbstractAction {

	private static final long serialVersionUID = -4915127447162778036L;

	private CardInstance card;
	
	private RevealableMarkerInstance marker;

	public RevealOrderMarkerAction(CardInstance card, RevealableMarkerInstance marker) {
		super("Reveal this order marker");
		this.card = card;
		this.marker = marker;
	}

	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		String gameId = HexScapeCore.getInstance().getGameId();
		
		RevealMarkerMessage message = new RevealMarkerMessage(playerId, gameId, card.getId(), marker.getMarkerDefinitionId(), 1);
		CoreMessageBus.post(message);		
	}
	
	
}
