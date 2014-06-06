package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRemovedMessage extends AbstractMarkerMessage {
	
	private boolean allMarkers;

	@JsonCreator
	public MarkerRemovedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId, 
			@JsonProperty("number") int number, 
			@JsonProperty("allMarkers") boolean allMarkers) {
		super(playerId, gameId, cardId, markerId, number);
		this.allMarkers = allMarkers;
	}

	public boolean isAllMarkers() {
		return allMarkers;
	} 

}
