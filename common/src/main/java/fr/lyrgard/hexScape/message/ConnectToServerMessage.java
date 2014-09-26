package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectToServerMessage extends AbstractUserMessage {
	
	private String host;
	
	@JsonCreator
	public ConnectToServerMessage(@JsonProperty("userId") String userId, @JsonProperty("host") String host) {
		super(userId);
		this.host = host;
	}

	public String getHost() {
		return host;
	}

}
