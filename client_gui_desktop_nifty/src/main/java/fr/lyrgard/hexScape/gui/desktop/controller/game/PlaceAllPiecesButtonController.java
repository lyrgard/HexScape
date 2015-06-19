package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.gui.desktop.action.AddPieceAction;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class PlaceAllPiecesButtonController extends AbstractImageButtonController {
	
	private CardInstance card;

	@Override
	public void onClick() {
		new AddPieceAction(card.getPieceLeftToPlace().peek(), card).actionPerformed(null);
	}

	public CardInstance getCard() {
		return card;
	}

	public void setCard(CardInstance card) {
		this.card = card;
	}

}
