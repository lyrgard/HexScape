package fr.lyrgard.hexScape.event;

import fr.lyrgard.hexScape.model.map.Map;

public class MapLoadedEvent {
	
	private Map map;

	
	
	public MapLoadedEvent(Map map) {
		super();
		this.map = map;
	}



	public Map getMap() {
		return map;
	}
	
	
}
