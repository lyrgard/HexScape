package fr.lyrgard.hexscape.client.network;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.player.Player;

@WebSocket
public class ClientWebSocket {

	private final CountDownLatch closeLatch;

	private Player player;

	private Session session;

	public ClientWebSocket(Player player) {
		super();
		this.closeLatch = new CountDownLatch(1);
		this.player = player;
	}

	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration, unit);
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
		this.session = null;
		this.closeLatch.countDown();
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n", session);
		this.session = session;
		try {
			send(new UserInformationMessage(player.getName(), player.getColor()));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		try {
			AbstractMessage message = MessageJsonMapper.getInstance().fromJson(msg);
			System.out.println("received message " + message.getClass() + " from server");
			CoreMessageBus.post(message);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Session getSession() {
		return session;
	}


	public void send(AbstractMessage message) {
		try {
			session.getRemote().sendString(MessageJsonMapper.getInstance().toJson(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
