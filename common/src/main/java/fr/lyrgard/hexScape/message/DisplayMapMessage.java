package fr.lyrgard.hexScape.message;

public class DisplayMapMessage extends AbstractMessage {

	private boolean displayFigures;
	
	private String gameId;

	public DisplayMapMessage(String gameId, boolean displayFigures) {
		super();
		this.gameId = gameId;
		this.displayFigures = displayFigures;
	}

	public String getGameId() {
		return gameId;
	}

	public boolean isDisplayFigures() {
		return displayFigures;
	}
}
