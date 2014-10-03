package fr.lyrgard.hexScape.message;

public abstract class AbstractMarkerMessage extends AbstractMessage {

	private String markerId;
	
	private String cardId;
	

	public AbstractMarkerMessage(String cardId, String markerId) {
		this.cardId = cardId;
		this.markerId = markerId;
	}

	public String getCardId() {
		return cardId;
	}

	public String getMarkerId() {
		return markerId;
	}
}
