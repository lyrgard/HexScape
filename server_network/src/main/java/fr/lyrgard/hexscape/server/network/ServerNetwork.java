package fr.lyrgard.hexscape.server.network;

import org.eclipse.jetty.server.Server;

public class ServerNetwork { 

	private int port;

	public ServerNetwork(int port) {
		super();
		this.port = port;
	}
	
	public void start() throws Exception {
		Server server = new Server(port);
        ServerWebSocket wsHandler = new ServerWebSocket();
        server.setHandler(wsHandler);
        server.start();
        server.join();
	}
	
	public static void main(String... args){
		ServerNetwork serverNetwork = new ServerNetwork(4242);
		try {
			serverNetwork.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
