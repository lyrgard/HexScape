package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThrowDiceMessage extends AbstractUserMessage {

	private int number;
	private String diceTypeId;
	
	@JsonCreator
	public ThrowDiceMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("number") int number, 
			@JsonProperty("diceTypeId") String diceTypeId) {
		super(playerId);
		this.number = number;
		this.diceTypeId = diceTypeId;
	}
	
	public int getNumber() {
		return number;
	}
	public String getDiceTypeId() {
		return diceTypeId;
	}
}
