package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.card.Army;

public class ArmyLoadedMessage extends AbstractPlayerMessage {
	private Army army;

	@JsonCreator
	public ArmyLoadedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("army") Army army) {
		super(playerId);
		this.army = army;
	}
	
	public Army getArmy() {
		return army;
	}
}
