package fr.lyrgard.hexscape.client.network;

import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.player.User;

public class ClientNetwork {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientNetwork.class);
	
	private static final ClientNetwork INSTANCE = new ClientNetwork();
	
	public static ClientNetwork getInstance() {
		return INSTANCE;
	}
	
	private ClientNetwork() {
	}
	
	private ClientWebSocket socket;
	private WebSocketClient client;
	
	public void connect(User user, String url) {
		if (socket == null) {
			String destUri = "ws://" + url;
			client = new WebSocketClient();
			
			client.getPolicy().setMaxTextMessageSize(1000000);
			client.setConnectTimeout(5000);
			socket = new ClientWebSocket(user);
			try {
				client.start();
				URI echoUri = new URI(destUri);
				ClientUpgradeRequest request = new ClientUpgradeRequest();
				client.connect(socket, echoUri, request);
				LOGGER.info("Connecting to : %s%n", echoUri);
				
			} catch (Throwable t) {
				LOGGER.error("Error while connecting to server", t);
				client = null;
			}
		}
    }
	
	public void disconnect() {
		if (socket != null) {
			try {
				if (client.isStarted()) {
					client.stop();
				}
			} catch (Exception e) {
				LOGGER.error("Error while disconnecting from server", e);
			}
			socket = null;
			client = null;
		}
	}
	
	public void send(AbstractMessage message) {
		if (socket != null) {
			try {
				socket.getSession().getRemote().sendString(MessageJsonMapper.getInstance().toJson(message));
				System.out.println("Sent message " + message.getClass() + " to server");
			} catch (IOException e) {
				LOGGER.error("Error while sending message to server : " + message, e);
			}
		}
	}
}
