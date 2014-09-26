package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveMarkerMessage extends AbstractMarkerMessage {
	
	private String markerTypeId;
	
	private int number;
	
	private boolean allMarkers;

	@JsonCreator
	public RemoveMarkerMessage(
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId,
			@JsonProperty("number") int number) {
		super(cardId, markerId);
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
