package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceMarkerMessage extends AbstractMarkerMessage {

	private String hiddenMarkerTypeId;
	
	private String markerTypeId;
	
	private int number;
	
	@JsonCreator
	public PlaceMarkerMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerTypeId") String markerTypeId, 
			@JsonProperty("number") int number,
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId) {
		super(playerId, gameId, cardId, null);
		this.markerTypeId = markerTypeId;
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
		this.number = number;
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



	public void setHiddenMarkerTypeId(String hiddenMarkerTypeId) {
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}

	
}
