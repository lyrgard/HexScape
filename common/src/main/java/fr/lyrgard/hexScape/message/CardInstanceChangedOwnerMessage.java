package fr.lyrgard.hexScape.message;

public class CardInstanceChangedOwnerMessage extends AbstractMessage {
	
	private String oldCardId;
	
	private String newCardId;
	
	private String newOwnerId;
	
	public CardInstanceChangedOwnerMessage(String oldCardId, String newOwnerId, String newCardId) {
		super();
		this.oldCardId = oldCardId;
		this.newOwnerId = newOwnerId;
		this.newCardId = newCardId;
	}


	public String getNewOwnerId() {
		return newOwnerId;
	}


	public String getOldCardId() {
		return oldCardId;
	}


	public String getNewCardId() {
		return newCardId;
	}
	
	

}
