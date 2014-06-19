package fr.lyrgard.hexScape.message;

public class DisplayMapMessage extends AbstractMessage {

	private String gameId;

	public DisplayMapMessage(String gameId) {
		super();
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	
	
	
}
