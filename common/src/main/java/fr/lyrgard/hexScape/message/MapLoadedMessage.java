package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.Player;

public class MapLoadedMessage extends AbstractMessage {

	private Map map;

	public MapLoadedMessage(Player player, Map map) {
		super(player);
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
}
