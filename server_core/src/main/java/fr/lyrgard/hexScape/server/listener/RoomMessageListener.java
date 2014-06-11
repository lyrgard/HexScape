package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.RoomLeftMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexscape.server.network.ServerNetwork;


public class RoomMessageListener  {

	private static RoomMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new RoomMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private RoomMessageListener() {
	}

	
	@Subscribe public void onJoinRoom(JoinRoomMessage message) {
		String playerId = message.getPlayerId();
		String roomId = message.getRoomId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (player != null && room != null) {
			room.getPlayers().add(player);
			player.setRoom(room);
			
			RoomJoinedMessage returnMessage = new RoomJoinedMessage(room);
			ServerNetwork.getInstance().sendMessageToPlayer(returnMessage, playerId);
			
			PlayerJoinedRoomMessage broadcastMessage = new PlayerJoinedRoomMessage(playerId, player.getName(), player.getColor(), roomId);
			ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(broadcastMessage, roomId, playerId);
		}
	}
	
	@Subscribe public void onRoomLeft(RoomLeftMessage message) {
		String playerId = message.getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null) {
			Room room = player.getRoom();

			if (room != null) {
				room.getPlayers().remove(player);
				player.setRoom(null);

				RoomLeftMessage returnMessage = new RoomLeftMessage(playerId);
				ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(returnMessage, room.getId(), playerId);
			}
		}
	}
	
	@Subscribe public void onMessagePosted(MessagePostedMessage message) {
		String roomId = message.getRoomId();
		
		if (roomId != null) {
			ServerNetwork.getInstance().sendMessageToRoom(message, roomId);
		}
	}
}
