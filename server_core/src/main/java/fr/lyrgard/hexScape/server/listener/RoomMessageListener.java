package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.User;
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
		String userId = message.getSessionUserId();
		String roomId = message.getRoomId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (user != null && room != null) {
			room.getUsers().add(user);
			user.setRoom(room);
			
			RoomJoinedMessage returnMessage = new RoomJoinedMessage(room);
			ServerNetwork.getInstance().sendMessageToUser(returnMessage, user.getId());
			
			UserJoinedRoomMessage broadcastMessage = new UserJoinedRoomMessage(user.getId(), user.getName(), user.getColor(), roomId);
			ServerNetwork.getInstance().sendMessageToRoomExceptUser(broadcastMessage, roomId, user.getId());
		}
	}
	
	@Subscribe public void onRoomMessagePosted(RoomMessagePostedMessage message) {
		String userId = message.getSessionUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getRoom() != null) {
			ServerNetwork.getInstance().sendMessageToRoom(message, user.getRoom().getId());
		}
	}
}
