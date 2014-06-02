package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.player.Player;

public class ErrorMessage extends AbstractMessage {

	private String message;

	
	
	public ErrorMessage(Player player, String message) {
		super(player);
		this.message = message;
	}



	public String getMessage() {
		return message;
	}
}
