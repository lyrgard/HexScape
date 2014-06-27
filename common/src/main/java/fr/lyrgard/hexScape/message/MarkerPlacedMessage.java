package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerPlacedMessage extends AbstractMarkerMessage {

	private String markerTypeId;
	
	private String hiddenMarkerTypeId;
	
	private int number;
	
	@JsonCreator
	public MarkerPlacedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId,
			@JsonProperty("markerTypeId") String markerTypeId,
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId,
			@JsonProperty("number") int number) {
		super(playerId, gameId, cardId, markerId);
		this.markerTypeId = markerTypeId;
		this.number = number;
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}

	public String getMarkerTypeId() {
		return markerTypeId;
	}

	public int getNumber() {
		return number;
	}

	public String getHiddenMarkerTypeId() {
		return hiddenMarkerTypeId;
	}

}
