package fr.lyrgard.hexScape.server.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.server.service.GameService;
import fr.lyrgard.hexscape.server.network.ServerNetwork;


public class RoomMessageListener  {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RoomMessageListener.class);

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
			
			try {
				Room clone = Room.fromJson(room.toJson());
				for (Game game : clone.getGames()) {
					GameService.removeUnseeableHiddenMarkersInfos(game, userId);
				}
				
				RoomJoinedMessage returnMessage = new RoomJoinedMessage(room);
				ServerNetwork.getInstance().sendMessageToUser(returnMessage, user.getId());
				
				UserJoinedRoomMessage broadcastMessage = new UserJoinedRoomMessage(user.getId(), user.getName(), user.getColor(), roomId);
				ServerNetwork.getInstance().sendMessageToRoomExceptUser(broadcastMessage, roomId, userId);
			} catch (IOException e) {
				LOGGER.error("Error while cloning room", e);
			}
			
			
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
