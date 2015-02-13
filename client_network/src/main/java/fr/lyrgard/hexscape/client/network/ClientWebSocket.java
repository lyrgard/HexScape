package fr.lyrgard.hexscape.client.network;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.player.User;

@WebSocket
public class ClientWebSocket {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientWebSocket.class);

	private final CountDownLatch closeLatch;

	private User user;

	private Session session;
	
	private HeartBeatGenerator heartBeatGenerator = new HeartBeatGenerator();

	public ClientWebSocket(User user) {
		super();
		this.closeLatch = new CountDownLatch(1);
		this.user = user;
	}

	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration, unit);
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		LOGGER.info("Connection closed: %d - %s%n", statusCode, reason);
		heartBeatGenerator.stop();
		this.session = null;
		this.closeLatch.countDown();
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		LOGGER.info("Got connect: %s%n", session);
		this.session = session;
		try {
			send(new UserInformationMessage(user.getName(), user.getColor()));
			
			new Thread(heartBeatGenerator).start();
		} catch (Throwable t) {
			LOGGER.error("Error on seding to websocket", t);
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		try {
			AbstractMessage message = MessageJsonMapper.getInstance().fromJson(msg);
			LOGGER.debug("received message " + message.getClass() + " from server");
			CoreMessageBus.post(message);
		} catch (JsonParseException e) {
			LOGGER.error("Error while receving message : " + msg, e);
		} catch (JsonMappingException e) {
			LOGGER.error("Error while receving message : " + msg, e);
		} catch (IOException e) {
			LOGGER.error("Error while receving message : " + msg, e);
		}
	}
	
	@OnWebSocketError
	public void onError(Throwable t) {
		LOGGER.error("Error on websocket", t);
	}

	public Session getSession() {
		return session;
	}


	
	public void send(AbstractMessage message) {
		try {
			session.getRemote().sendString(MessageJsonMapper.getInstance().toJson(message));
		} catch (IOException e) {
			LOGGER.error("Error while sending message : " + message, e);
		}
	}
}
