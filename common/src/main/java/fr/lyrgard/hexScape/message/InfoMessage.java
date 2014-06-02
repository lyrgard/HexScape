package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.player.Player;

public class InfoMessage extends AbstractMessage {

	private String message;

	
	
	public InfoMessage(Player player, String message) {
		super(player);
		this.message = message;
	}



	public String getMessage() {
		return message;
	}
}
