package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerPlacedMessage extends AbstractMarkerMessage {
	
	private String playerId;

	private String markerTypeId;
	
	private String hiddenMarkerTypeId;
	
	private int number;
	
	@JsonCreator
	public MarkerPlacedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId,
			@JsonProperty("markerTypeId") String markerTypeId,
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId,
			@JsonProperty("number") int number) {
		super(cardId, markerId);
		this.playerId = playerId;
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

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

}
