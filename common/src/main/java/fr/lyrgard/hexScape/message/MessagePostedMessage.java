package fr.lyrgard.hexScape.message;

public class MessagePostedMessage extends AbstractMessage {

	private String message;
	
	private String roomId;
	
	private String gameId;
	
	public MessagePostedMessage(String playerId, String message, String roomId,
			String gameId) {
		super(playerId);
		this.message = message;
		this.roomId = roomId;
		this.gameId = gameId;
	}

	public String getMessage() {
		return message;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getGameId() {
		return gameId;
	}
	
	
}
