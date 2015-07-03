package fr.lyrgard.hexScape.gui.desktop.message;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class DisplayCardDetailMessage extends AbstractMessage {

	private CardInstance card;

	
	
	public DisplayCardDetailMessage(CardInstance card) {
		super();
		this.card = card;
	}

	public CardInstance getCard() {
		return card;
	}
	
	
}
