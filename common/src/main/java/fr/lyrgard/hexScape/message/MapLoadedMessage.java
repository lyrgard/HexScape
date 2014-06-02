package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.map.Map;

public class MapLoadedMessage extends AbstractMessage {

	private Map map;

	public MapLoadedMessage(String playerId, Map map) {
		super(playerId);
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
}
