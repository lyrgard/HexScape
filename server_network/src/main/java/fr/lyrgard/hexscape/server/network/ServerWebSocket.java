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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.AbstractUserMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.HeartBeatMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexscape.server.network.id.IdGenerator;

@WebSocket
public class ServerWebSocket extends WebSocketHandler {

	private String playerId;

	private Session session;

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
		ServerNetwork.getInstance().unRegisterSocket(playerId);
		
		// Make the user to leave room
		DisconnectedFromServerMessage message = new DisconnectedFromServerMessage(playerId);
		CoreMessageBus.post(message);
		
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		System.out.println("Error: " + t.getMessage());
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;
		playerId = IdGenerator.getInstance().getNewUserId(); 
		
		
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
				Player player = new Player(name, color);
				player.setId(playerId);
				Universe.getInstance().getPlayersByIds().put(playerId, player);
				System.out.println("User connected : " + playerId + ",  " + player.getName() + ", " + player.getColor() );
				send(new UserIdAllocatedMessage(playerId));
				ServerNetwork.getInstance().registerSocket(playerId, this);
			} else {
				System.out.println("Received message " + message.getClass() + " from player " + playerId);
				if (message instanceof AbstractUserMessage) {
					((AbstractUserMessage)message).setPlayerId(playerId);
				}
				CoreMessageBus.post(message);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(ServerWebSocket.class);
	}

	public void send(AbstractMessage message) {
		try {
			session.getRemote().sendString(MessageJsonMapper.getInstance().toJson(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
