package fr.lyrgard.hexScape.message;

public abstract class AbstractMessage {
	
	private String playerId;

	public AbstractMessage(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}
}
