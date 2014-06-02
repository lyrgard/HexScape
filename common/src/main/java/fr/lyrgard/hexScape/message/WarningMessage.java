package fr.lyrgard.hexScape.message;


public class WarningMessage extends AbstractMessage {

	private String message;

	
	
	public WarningMessage(String playerId, String message) {
		super(playerId);
		this.message = message;
	}



	public String getMessage() {
		return message;
	}
}
