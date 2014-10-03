package fr.lyrgard.hexScape.message;

public class AbstractUserMessage extends AbstractMessage {

	private String userId;
	
	public AbstractUserMessage(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
