package fr.lyrgard.hexscape.client.network;

import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.player.User;

public class ClientNetwork {
	
	private static final ClientNetwork INSTANCE = new ClientNetwork();
	
	private HeartBeatGenerator heartBeatGenerator = new HeartBeatGenerator();
	
	public static ClientNetwork getInstance() {
		return INSTANCE;
	}
	
	private ClientNetwork() {
	}
	
	ClientWebSocket socket;
	WebSocketClient client;
	
	public void connect(User user, String url) {
		if (socket == null) {
			String destUri = "ws://" + url;
			client = new WebSocketClient();
			client.getPolicy().setMaxTextMessageSize(1000000);
			socket = new ClientWebSocket(user);
			try {
				client.start();
				URI echoUri = new URI(destUri);
				ClientUpgradeRequest request = new ClientUpgradeRequest();
				client.connect(socket, echoUri, request);
				System.out.printf("Connecting to : %s%n", echoUri);
				
				new Thread(heartBeatGenerator).start();
			} catch (Throwable t) {
				t.printStackTrace();
				client = null;
			}
		}
    }
	
	public void disconnect() {
		if (socket != null) {
			heartBeatGenerator.stop();
			try {
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			socket = null;
		}
	}
	
	public void send(AbstractMessage message) {
		if (socket != null) {
			try {
				socket.getSession().getRemote().sendString(MessageJsonMapper.getInstance().toJson(message));
				System.out.println("Sent message " + message.getClass() + " to server");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
