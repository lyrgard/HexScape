package fr.lyrgard.hexScape.gui.desktop.message;

import fr.lyrgard.hexScape.message.AbstractMessage;

public class CardSelectedMessage extends AbstractMessage {

	private String cardTypeId;

	public CardSelectedMessage(String cardTypeId) {
		super();
		this.cardTypeId = cardTypeId;
	}
	
	public String getCardTypeId() {
		return cardTypeId;
	}

	
	
	
}
