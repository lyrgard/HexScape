package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRevealedMessage extends AbstractMarkerMessage {

	private String playerId;
	
	private String hiddenMarkerTypeId;
	
	@JsonCreator
	public MarkerRevealedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId,
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId) {
		super(cardId, markerId);
		this.playerId = playerId;
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}

	public String getHiddenMarkerTypeId() {
		return hiddenMarkerTypeId;
	}

	public String getPlayerId() {
		return playerId;
	}

}
