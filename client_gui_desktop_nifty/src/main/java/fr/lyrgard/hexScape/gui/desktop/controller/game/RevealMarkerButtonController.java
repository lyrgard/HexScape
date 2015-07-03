package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.RevealMarkerMessage;

public class RevealMarkerButtonController extends AbstractImageButtonController {

	public static final String MARKER_ID = "markerId";
	public static final String CARD_ID = "cardId";
	
	@Override
	public void onClick() {
		String markerId = attributes.get(MARKER_ID);
		String cardId = attributes.get(CARD_ID);
	
		RevealMarkerMessage message = new RevealMarkerMessage(cardId, markerId);
		CoreMessageBus.post(message);
	}
}
