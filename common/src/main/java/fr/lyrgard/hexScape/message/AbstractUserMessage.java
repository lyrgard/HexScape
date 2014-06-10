package fr.lyrgard.hexScape.message;
 
public abstract class AbstractUserMessage extends AbstractMessage{
	
	private String playerId;
	


	public AbstractUserMessage(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
}
