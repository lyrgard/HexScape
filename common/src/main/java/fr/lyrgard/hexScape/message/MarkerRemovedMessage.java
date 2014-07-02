package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRemovedMessage extends AbstractMarkerMessage {
	
	private int number;

	@JsonCreator
	public MarkerRemovedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId, 
			@JsonProperty("number") int number) {
		super(playerId, gameId, cardId, markerId);
		this.number = number;
	}

	public int getNumber() {
		return number;
	} 

}
