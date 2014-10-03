package fr.lyrgard.hexScape.message;
 
public abstract class AbstractPlayerMessage extends AbstractMessage{
	
	private String playerId;
	


	public AbstractPlayerMessage(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
}
