package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRemovedMessage extends AbstractMarkerMessage {
	
	private int number;
	
	private boolean allMarkers;

	@JsonCreator
	public MarkerRemovedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId, 
			@JsonProperty("number") int number, 
			@JsonProperty("allMarkers") boolean allMarkers) {
		super(playerId, gameId, cardId, markerId);
		this.allMarkers = allMarkers;
		this.number = number;
	}

	public boolean isAllMarkers() {
		return allMarkers;
	}

	public int getNumber() {
		return number;
	} 

}
