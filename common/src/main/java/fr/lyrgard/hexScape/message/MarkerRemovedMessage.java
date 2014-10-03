package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkerRemovedMessage extends AbstractMarkerMessage {
	
	private String playerId;
	
	private int number;

	@JsonCreator
	public MarkerRemovedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerId") String markerId, 
			@JsonProperty("number") int number) {
		super(cardId, markerId);
		this.playerId = playerId;
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public String getPlayerId() {
		return playerId;
	} 

}
