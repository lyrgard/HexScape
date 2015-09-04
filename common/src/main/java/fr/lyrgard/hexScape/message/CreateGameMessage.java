package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.map.Map;

public class CreateGameMessage extends AbstractMessage {

	private String name;
	
	private String description;
	
	private Map map;
	
	private int playerNumber;

	@JsonCreator
	public CreateGameMessage( 
			@JsonProperty("name") String name,
			@JsonProperty("description") String description, 
			@JsonProperty("map") Map map,
			@JsonProperty("playerNumber") int playerNumber) {
		this.name = name;
		this.map = map;
		this.playerNumber = playerNumber;
	}
	

	public Map getMap() {
		return map;
	}
	
	public String getDescription() {
		return description;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public String getName() {
		return name;
	}
}
