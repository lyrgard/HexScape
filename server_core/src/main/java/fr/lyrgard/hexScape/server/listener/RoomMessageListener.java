package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.PostMessageMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexscape.server.network.ServerNetwork;


public class RoomMessageListener  {

private static RoomMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new RoomMessageListener();
			MessageBus.register(instance);
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
			ServerNetwork.getInstance().sendMessageToRoom(broadcastMessage, roomId);
		}
	}
	
	@Subscribe public void onPostMessage(PostMessageMessage message) {
		String playerId = message.getPlayerId();
		String roomId = message.getRoomId();
		String messageContent = message.getMessage();
		
		if (roomId != null) {
			MessagePostedMessage returnMessage = new MessagePostedMessage(playerId, messageContent, roomId, null);
			ServerNetwork.getInstance().sendMessageToRoom(returnMessage, roomId);
		}
	}
}
