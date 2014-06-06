package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectToServerMessage extends AbstractUserMessage {
	
	private String host;
	
	private int port;
	
	@JsonCreator
	public ConnectToServerMessage(@JsonProperty("playerId") String playerId, @JsonProperty("host") String host, @JsonProperty("port") int port) {
		super(playerId);
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
