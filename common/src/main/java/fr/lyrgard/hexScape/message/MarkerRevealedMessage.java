package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRevealedMessage extends AbstractMarkerMessage {

	private String hiddenMarkerTypeId;
	
	@JsonCreator
	public MarkerRevealedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId,
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId) {
		super(playerId, gameId, cardId, markerId);
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}

	public String getHiddenMarkerTypeId() {
		return hiddenMarkerTypeId;
	}

}
