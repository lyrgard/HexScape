package fr.lyrgard.hexscape.server.network;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;



public class ServerNetwork { 
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ServerNetwork.class);

	private static final ServerNetwork INSTANCE = new ServerNetwork();
	
	public static ServerNetwork getInstance() {
		return INSTANCE;
	}
	
	private ServerNetwork() {
	}
	
	private Map<String, ServerWebSocket> socketsById = new HashMap<>();  
	
	public void start(int port) throws Exception {
		Server server = new Server(port);
        ServerWebSocket wsHandler = new ServerWebSocket();
        server.setHandler(wsHandler);
        server.start();
        server.join();
	}
	
	public static void main(String... args){
		ServerNetwork serverNetwork = new ServerNetwork();
		try {
			serverNetwork.start(4242);
		} catch (Exception e) {
			LOGGER.error("Error while starting the server on default port 4242", e);
		}
	}
	
	public void registerSocket(String playerId, ServerWebSocket socket) {
		socketsById.put(playerId, socket);
	}
	
	public void unRegisterSocket(String playerId) {
		socketsById.remove(playerId);
	}

	public void sendMessageToUser(AbstractMessage message, String userId) {
		ServerWebSocket socket = socketsById.get(userId);
		if (socket != null) {
			socket.send(message);
			//System.out.println("Sent message " + message.getClass() + " to player " + playerId);
		} else {
			System.out.println("Wanted to send message " + message.getClass() + " to user " + userId + " but no socket found");
		}
	}
	
	public void sendMessageToRoom(AbstractMessage message, String roomId) {
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (room != null) {
			for (User user : room.getUsers()) {
				sendMessageToUser(message, user.getId());
			}
		}
	}
	
	public void sendMessageToRoomExceptUser(AbstractMessage message, String roomId, String userId) {
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (room != null) {
			for (User user : room.getUsers()) {
				if (!userId.equals(user.getId())) {
					sendMessageToUser(message, user.getId());
				}
			}
		}
	}
	
	public void sendMessageToGame(AbstractMessage message, String gameId) {
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			for (Player player : game.getPlayers()) {
				if (player.getUserId() != null) 
				sendMessageToUser(message, player.getUserId());
			}
			for (String userId : game.getObserversIds()) {
				sendMessageToUser(message, userId);
			}
		}
	}
	
	public void sendMessageToGameExceptUser(AbstractMessage message, String gameId, String userId) {
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			for (Player otherPlayer : game.getPlayers()) {
				if (otherPlayer.getUserId() != null && !otherPlayer.getUserId().equals(userId)) {
					sendMessageToUser(message, otherPlayer.getUserId());
				}
			}
			for (String otherUserId : game.getObserversIds()) {
				sendMessageToUser(message, otherUserId);
			}
		}
	}
}
