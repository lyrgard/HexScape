package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RevealMarkerMessage extends AbstractMarkerMessage {

	@JsonCreator
	public RevealMarkerMessage(
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId) {
		super(cardId, markerId);
	}

}
