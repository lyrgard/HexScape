package fr.lyrgard.hexScape.event.game;

import fr.lyrgard.hexScape.model.player.Player;

public class GameMessagePostedEvent {

	private Player player;
	
	private String gameId;
	
	private String message;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
