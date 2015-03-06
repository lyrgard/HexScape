package fr.lyrgard.hexScape.message;

public class ChangeCardInstanceOwnerMessage extends AbstractMessage {
	
	private String cardId;
	
	private String newOwnerId;
	
	public ChangeCardInstanceOwnerMessage(String cardId, String newOwnerId) {
		super();
		this.cardId = cardId;
		this.newOwnerId = newOwnerId;
	}

	public String getCardId() {
		return cardId;
	}

	public String getNewOwnerId() {
		return newOwnerId;
	}
	
	

}
