package fr.lyrgard.hexScape.message;

public class ErrorMessage extends AbstractMessage {

	private String message;

	
	
	public ErrorMessage(String playerId, String message) {
		super(playerId);
		this.message = message;
	}



	public String getMessage() {
		return message;
	}
}
