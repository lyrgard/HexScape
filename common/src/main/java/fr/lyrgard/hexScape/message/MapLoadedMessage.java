package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.map.Map;

public class MapLoadedMessage extends AbstractUserMessage {

	private Map map;

	@JsonCreator
	public MapLoadedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("map") Map map) {
		super(playerId);
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
}
