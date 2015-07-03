package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.ChangeCardInstanceOwnerMessage;

public class GiveCardToPlayerButtonController extends AbstractImageButtonController {

	public static final String PLAYER_ID = "playerId";
	public static final String CARD_ID = "cardId";
	
	@Override
	public void onClick() {
		String playerId = attributes.get(PLAYER_ID);
		String cardId = attributes.get(CARD_ID);
		
		CoreMessageBus.post(new ChangeCardInstanceOwnerMessage(cardId, playerId));
	}

}
