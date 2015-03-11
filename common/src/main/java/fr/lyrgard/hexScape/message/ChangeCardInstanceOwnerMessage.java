package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeCardInstanceOwnerMessage extends AbstractMessage {
	
	private String cardId;
	
	private String newOwnerId;
	
	@JsonCreator
	public ChangeCardInstanceOwnerMessage(
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("newOwnerId") String newOwnerId) {
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
