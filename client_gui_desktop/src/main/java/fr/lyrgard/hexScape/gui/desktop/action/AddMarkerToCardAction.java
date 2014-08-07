package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;

public class AddMarkerToCardAction extends AbstractAction {

	private static final long serialVersionUID = -5302251867512312354L;

	private MarkerDefinition markerType;
	
	private HiddenMarkerDefinition hiddenMarkerType;
	
	private CardInstance card;
	
	
	
	public AddMarkerToCardAction(MarkerDefinition markerType, HiddenMarkerDefinition hiddenMarkerType, CardInstance card) {
		super("Add a " + markerType.getName() + " to this card");
		this.markerType = markerType;
		this.card = card;
		this.hiddenMarkerType = hiddenMarkerType;
		final ImageIcon icon = new ImageIcon(markerType.getImage().getAbsolutePath());
		putValue(Action.SMALL_ICON, icon);
		
	}

	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		String gameId = HexScapeCore.getInstance().getGameId();
		String hiddenMarkerTypeId = null;
		PlaceMarkerMessage message = null;
		if (hiddenMarkerType != null) {
			hiddenMarkerTypeId = hiddenMarkerType.getId();
			message = new PlaceMarkerMessage(playerId, gameId, card.getId(), hiddenMarkerTypeId, 1, markerType.getId());
		} else {
			message = new PlaceMarkerMessage(playerId, gameId, card.getId(), markerType.getId(), 1, hiddenMarkerTypeId);
		}
		
		
		CoreMessageBus.post(message);
	}

}
