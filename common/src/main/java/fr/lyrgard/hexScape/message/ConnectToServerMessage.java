package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectToServerMessage extends AbstractUserMessage {
	
	private String host;
	
	@JsonCreator
	public ConnectToServerMessage(@JsonProperty("playerId") String playerId, @JsonProperty("host") String host) {
		super(playerId);
		this.host = host;
	}

	public String getHost() {
		return host;
	}

}
