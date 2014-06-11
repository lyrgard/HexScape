package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.map.Map;

public class CreateGameMessage extends AbstractUserMessage {

	private String name;
	
	private Map map;
	
	private int playerNumber;

	@JsonCreator
	private CreateGameMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("name") String name, 
			@JsonProperty("map") Map map,
			@JsonProperty("playerNumber") int playerNumber) {
		super(playerId);
		this.name = name;
		this.map = map;
		this.playerNumber = playerNumber;
	}

	public Map getMap() {
		return map;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public String getName() {
		return name;
	}
}
