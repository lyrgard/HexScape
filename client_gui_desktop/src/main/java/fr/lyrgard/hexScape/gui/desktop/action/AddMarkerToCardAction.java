package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;

public class AddMarkerToCardAction extends AbstractAction {

	private static final long serialVersionUID = -5302251867512312354L;

	private MarkerDefinition marker;
	
	private CardInstance card;
	
	
	
	public AddMarkerToCardAction(MarkerDefinition marker, CardInstance card) {
		super("Add a " + marker.getName() + " to this card");
		this.marker = marker;
		this.card = card;
		final ImageIcon icon = new ImageIcon(marker.getImage().getAbsolutePath());
		putValue(Action.SMALL_ICON, icon);
		
	}

	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		String gameId = HexScapeCore.getInstance().getGameId();
		
		PlaceMarkerMessage message = new PlaceMarkerMessage(playerId, gameId, card.getId(), marker.getId(), 1);
		MessageBus.post(message);
	}

}
