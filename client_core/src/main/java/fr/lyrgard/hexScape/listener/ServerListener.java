package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.CannotConnectToServerMessage;
import fr.lyrgard.hexScape.message.ConnectToServerMessage;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public class ServerListener {

	private static ServerListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ServerListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private ServerListener() {
	}
	
	@Subscribe public void onConnectToServerMessage(ConnectToServerMessage message) {
		String host = message.getHost();
		String userId = CurrentUserInfo.getInstance().getId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null) {
			try {
				ClientNetwork.getInstance().connect(user, host);
			} catch (Exception e) {
				GuiMessageBus.post(new CannotConnectToServerMessage());
			}
		}
	}
	
	@Subscribe public void onDisconnectFromServerMessage(DisconnectFromServerMessage message) {
		ClientNetwork.getInstance().disconnect();
		
		String oldUserId = CurrentUserInfo.getInstance().getId();
		User user = Universe.getInstance().getUsersByIds().get(oldUserId);
		
		Universe.getInstance().getGamesByGameIds().clear();
		Universe.getInstance().getRoomsByRoomIds().clear();
		Universe.getInstance().getUsersByIds().clear();
		
		String newUserId = "1";
		CurrentUserInfo.getInstance().setId(newUserId);
		user.setId(newUserId);
		Universe.getInstance().getUsersByIds().put(newUserId, user);
		
		HexScapeCore.getInstance().setOnline(false);
		DisconnectedFromServerMessage resultMessage = new DisconnectedFromServerMessage(newUserId);
		GuiMessageBus.post(resultMessage);
	}


	@Subscribe public void onUserIdAllocated(UserIdAllocatedMessage message) {
		String userId = message.getUserId();
		String oldUserId = CurrentUserInfo.getInstance().getId();
		
		User user = Universe.getInstance().getUsersByIds().get(oldUserId);
		
		if (user != null) {
			user.setId(userId);
			Universe.getInstance().getUsersByIds().put(userId, user);
		}
		CurrentUserInfo.getInstance().setId(userId);
		
		if (user != null) {
			Universe.getInstance().getUsersByIds().remove(oldUserId);
		}
		ConnectedToServerMessage resultMessage = new ConnectedToServerMessage(userId);
		CoreMessageBus.post(resultMessage);
	}
	
	@Subscribe public void onConnectedToServer(ConnectedToServerMessage message) {

		HexScapeCore.getInstance().setOnline(true);
		GuiMessageBus.post(message);
		
		JoinRoomMessage resultMessage = new JoinRoomMessage(Room.DEFAULT_ROOM_ID); 
		ClientNetwork.getInstance().send(resultMessage);
	}
	
}
