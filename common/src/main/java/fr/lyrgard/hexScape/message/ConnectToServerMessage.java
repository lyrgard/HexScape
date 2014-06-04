package fr.lyrgard.hexScape.message;

public class ConnectToServerMessage extends AbstractMessage {
	
	private String host;
	
	private int port;
	
	public ConnectToServerMessage(String playerId, String host, int port) {
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
