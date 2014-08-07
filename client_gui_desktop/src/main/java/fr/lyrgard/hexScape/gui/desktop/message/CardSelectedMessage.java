package fr.lyrgard.hexScape.gui.desktop.message;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class CardSelectedMessage extends AbstractMessage {

	private CardInstance card;

	public CardSelectedMessage(CardInstance card) {
		super();
		this.card = card;
	}

	public CardInstance getCard() {
		return card;
	}
	
	

	
	
	
}
