package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveMarkerMessage extends AbstractMarkerMessage {
	
	private String markerTypeId;
	
	private int number;
	
	private boolean allMarkers;

	@JsonCreator
	public RemoveMarkerMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId,
			@JsonProperty("markerTypeId") String markerTypeId,
			@JsonProperty("number") int number, 
			@JsonProperty("allMarkers") boolean allMarkers) {
		super(playerId, gameId, cardId, markerId);
		this.markerTypeId = markerTypeId;
		this.allMarkers = allMarkers;
		this.number = number;
	}

	public boolean isAllMarkers() {
		return allMarkers;
	}

	public int getNumber() {
		return number;
	}

	public String getMarkerTypeId() {
		return markerTypeId;
	} 

}
