package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.PlacePieceMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class PlaceAllPiecesButtonController extends AbstractImageButtonController {
	
	private CardInstance card;

	@Override
	public void onClick() {
		PlacePieceMessage message = new PlacePieceMessage(card.getId(), card.getPieceLeftToPlace().peek());
		CoreMessageBus.post(message);	
	}

	public CardInstance getCard() {
		return card;
	}

	public void setCard(CardInstance card) {
		this.card = card;
	}

}
