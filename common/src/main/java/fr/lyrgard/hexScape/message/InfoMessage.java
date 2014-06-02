package fr.lyrgard.hexScape.message;

public class InfoMessage extends AbstractMessage {

	private String message;

	
	
	public InfoMessage(String playerId, String message) {
		super(playerId);
		this.message = message;
	}



	public String getMessage() {
		return message;
	}
}
