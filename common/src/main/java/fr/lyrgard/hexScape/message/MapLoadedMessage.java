package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.map.Map;

public class MapLoadedMessage extends AbstractUserMessage {

	private Map map;

	@JsonCreator
	public MapLoadedMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("map") Map map) {
		super(userId);
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
}
