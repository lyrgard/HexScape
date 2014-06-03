package fr.lyrgard.hexScape.message;

public abstract class AbstractMarkerMessage extends AbstractGameMessage {

	private String cardId;
	
	private String markerId;
	
	private int number;
	

	public AbstractMarkerMessage(String playerId, String gameId, String cardId,
			String markerId, int number) {
		super(playerId, gameId);
		this.cardId = cardId;
		this.markerId = markerId;
		this.number = number;
	}

	public String getCardId() {
		return cardId;
	}

	public String getMarkerId() {
		return markerId;
	}

	public int getNumber() {
		return number;
	}
}
