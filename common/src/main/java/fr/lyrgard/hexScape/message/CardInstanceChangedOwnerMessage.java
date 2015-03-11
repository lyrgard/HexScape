package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardInstanceChangedOwnerMessage extends AbstractMessage {
	
	private String oldCardId;
	
	private String newCardId;
	
	private String newOwnerId;
	
	private String oldOwnerId;
	
	@JsonCreator
	public CardInstanceChangedOwnerMessage(
			@JsonProperty("oldCardId") String oldCardId, 
			@JsonProperty("oldOwnerId") String oldOwnerId, 
			@JsonProperty("newCardId") String newCardId, 
			@JsonProperty("newOwnerId") String newOwnerId) {
		super();
		this.oldCardId = oldCardId;
		this.newCardId = newCardId;
		this.oldOwnerId = oldOwnerId;
		this.newOwnerId = newOwnerId;
		
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


	public String getOldOwnerId() {
		return oldOwnerId;
	}
	
	

}
