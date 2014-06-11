package fr.lyrgard.hexscape.server.network;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;



public class ServerNetwork { 

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
			e.printStackTrace();
		}
	}
	
	public void registerSocket(String playerId, ServerWebSocket socket) {
		socketsById.put(playerId, socket);
	}
	
	public void unRegisterSocket(String playerId) {
		socketsById.remove(playerId);
	}

	public void sendMessageToPlayer(AbstractMessage message, String playerId) {
		ServerWebSocket socket = socketsById.get(playerId);
		if (socket != null) {
			socket.send(message);
			System.out.println("Sent message " + message.getClass() + " to player " + playerId);
		} else {
			System.out.println("Wanted to send message " + message.getClass() + " to player " + playerId + " but no socket found");
		}
	}
	
	public void sendMessageToRoom(AbstractMessage message, String roomId) {
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (room != null) {
			for (Player player : room.getPlayers()) {
				sendMessageToPlayer(message, player.getId());
			}
		}
	}
	
	public void sendMessageToRoomExceptPlayer(AbstractMessage message, String roomId, String playerId) {
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (room != null) {
			for (Player player : room.getPlayers()) {
				if (!playerId.equals(player.getId())) {
					sendMessageToPlayer(message, player.getId());
				}
			}
		}
	}
	
	public void sendMessageToGame(AbstractMessage message, String gameId) {
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			for (String playerId : game.getPlayersIds()) {
				sendMessageToPlayer(message, playerId);
			}
			for (String playerId : game.getObserversIds()) {
				sendMessageToPlayer(message, playerId);
			}
		}
	}
}
