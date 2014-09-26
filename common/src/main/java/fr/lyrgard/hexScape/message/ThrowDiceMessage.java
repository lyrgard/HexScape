package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThrowDiceMessage extends AbstractPlayerMessage {

	private int number;
	private String diceTypeId;
	private int numberOfFaces;
	
	@JsonCreator
	public ThrowDiceMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("number") int number, 
			@JsonProperty("diceTypeId") String diceTypeId,
			@JsonProperty("numberOfFaces") int numberOfFaces) {
		super(playerId);
		this.number = number;
		this.diceTypeId = diceTypeId;
		this.numberOfFaces = numberOfFaces;
	}
	
	public int getNumber() {
		return number;
	}
	public String getDiceTypeId() {
		return diceTypeId;
	}

	public int getNumberOfFaces() {
		return numberOfFaces;
	}
}
