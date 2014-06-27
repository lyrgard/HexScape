package fr.lyrgard.hexScape.message;

public abstract class AbstractMarkerMessage extends AbstractGameMessage {

	private String markerId;
	
	private String cardId;
	

	public AbstractMarkerMessage(String playerId, String gameId, String cardId,
			String markerId) {
		super(playerId, gameId);
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
