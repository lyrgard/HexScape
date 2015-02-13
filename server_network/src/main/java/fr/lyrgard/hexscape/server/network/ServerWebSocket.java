package fr.lyrgard.hexscape.server.network;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.HeartBeatMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.IdGenerator;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.User;

@WebSocket
public class ServerWebSocket extends WebSocketHandler {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ServerWebSocket.class);

	private String userId;

	private Session session;

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
		ServerNetwork.getInstance().unRegisterSocket(userId);
		
		// Make the user to leave room
		DisconnectedFromServerMessage message = new DisconnectedFromServerMessage(userId);
		message.setSessionUserId(userId);
		CoreMessageBus.post(message);
		
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		System.out.println("Error: " + t.getMessage());
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;
		userId = IdGenerator.getInstance().getNewUserId(); 		
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		try {
			AbstractMessage message = MessageJsonMapper.getInstance().fromJson(msg);
			if (message instanceof HeartBeatMessage) {
				//System.out.println("Heart beat from player " + playerId);
			} else if (message instanceof UserInformationMessage) {
				String name = ((UserInformationMessage) message).getName();
				ColorEnum color = ((UserInformationMessage) message).getColor();
				User user = new User();
				user.setName(name);
				user.setColor(color);
				user.setId(userId);
				Universe.getInstance().getUsersByIds().put(userId, user);
				System.out.println("User connected : " + userId + ",  " + user.getName() + ", " + user.getColor() );
				send(new UserIdAllocatedMessage(userId));
				ServerNetwork.getInstance().registerSocket(userId, this);
			} else {
				//System.out.println("Received message " + message.getClass() + " from player " + playerId);
				message.setSessionUserId(userId);
				CoreMessageBus.post(message);
			}
		} catch (JsonParseException e) {
			LOGGER.error("Error while receiving message : " + msg, e);
		} catch (JsonMappingException e) {
			LOGGER.error("Error while receiving message : " + msg, e);
		} catch (IOException e) {
			LOGGER.error("Error while receiving message : " + msg, e);
		}
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setMaxTextMessageSize(1000000);
		factory.register(ServerWebSocket.class);
		
	}

	public void send(AbstractMessage message) {
		try {
			session.getRemote().sendString(MessageJsonMapper.getInstance().toJson(message));
		} catch (IOException e) {
			LOGGER.error("Error while sending message : " + message, e);
		}
	}


}
