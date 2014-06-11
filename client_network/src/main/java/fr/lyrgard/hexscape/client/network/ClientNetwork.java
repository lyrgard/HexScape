package fr.lyrgard.hexscape.client.network;

import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;

public class ClientNetwork {
	
	private static final ClientNetwork INSTANCE = new ClientNetwork();
	
	public static ClientNetwork getInstance() {
		return INSTANCE;
	}
	
	private ClientNetwork() {
	}
	
	ClientWebSocket socket;
	WebSocketClient client;

	public static void main(String[] args) {
		Player player = new Player("Player1", ColorEnum.BLUE);
		getInstance().connect(player, "localhost", 4242);
	}
	
	public void connect(Player player, String host, int port) {
		if (socket == null) {
			String destUri = "ws://" + host + ":" + port;
			client = new WebSocketClient();
			socket = new ClientWebSocket(player);
			try {
				client.start();
				URI echoUri = new URI(destUri);
				ClientUpgradeRequest request = new ClientUpgradeRequest();
				client.connect(socket, echoUri, request);
				System.out.printf("Connecting to : %s%n", echoUri);
				//socket.awaitClose(5, TimeUnit.SECONDS);
			} catch (Throwable t) {
				t.printStackTrace();
				client = null;
			}
		}
    }
	
	public void disconnect() {
		if (socket != null) {
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
