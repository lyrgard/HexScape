package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.RemoveMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class RemoveAllMarkersFromCardAction extends AbstractAction {
	
	private static final long serialVersionUID = 2139602334359226593L;

	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/remove.png"));
	
	private CardInstance card;
	
	public RemoveAllMarkersFromCardAction(CardInstance card) {
		super("Remove all markers from this card", icon);
		this.card = card;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		String gameId = HexScapeCore.getInstance().getGameId();
		
		RemoveMarkerMessage message = new RemoveMarkerMessage(playerId, gameId, card.getId(), null, 0, true);
		CoreMessageBus.post(message);
	}

}
