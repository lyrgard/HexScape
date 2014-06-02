package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.player.Player;

public abstract class AbstractMessage {
	
	private Player player;

	public AbstractMessage(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
