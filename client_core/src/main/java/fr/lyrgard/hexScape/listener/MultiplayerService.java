package fr.lyrgard.hexScape.listener;

import java.io.IOException;

import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Network;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ErrorEvent;
import fr.lyrgard.hexScape.model.message.RegisterMessageHelper;
import fr.lyrgard.hexScape.model.message.client.room.GetRoomQueryMessage;
import fr.lyrgard.hexScape.model.message.common.room.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.model.message.server.handshake.HandShakeCompleteMessage;
import fr.lyrgard.hexScape.model.message.server.handshake.UserIdMessage;
import fr.lyrgard.hexScape.model.message.server.room.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.model.message.server.room.PlayerLeftRoomMessage;
import fr.lyrgard.hexScape.model.message.server.room.RoomContentResponseMessage;

public class MultiplayerService {

private Client client;
	
	public void connectToServer(String url, int port) {
		try {

			client = Network.connectToServer(url, port);
			
			RegisterMessageHelper.registerMessages();
	        client.addClientStateListener(new ClientStateListener());
	        client.addMessageListener(new RoomMessageListener(), RoomContentResponseMessage.class);
	        client.addMessageListener(new RoomMessageListener(), PlayerJoinedRoomMessage.class);
	        client.addMessageListener(new RoomMessageListener(), PlayerLeftRoomMessage.class);
	        client.addMessageListener(new RoomMessageListener(), RoomMessagePostedMessage.class);
	        client.addMessageListener(new HandShakeListener(), UserIdMessage.class, HandShakeCompleteMessage.class);
			
			client.start();
	        
		} catch (IOException e) {
			HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("Unable to connect to server"));
			client = null;
		}
	}
	
	public void disconnect() {
		if (client != null) {
			client.close();
			client = null;
		}
	}

	public void requestRoomContent(String roomId) {
		GetRoomQueryMessage message = new GetRoomQueryMessage();
		message.setRoomId(roomId);
		client.send(message);
	}
	
	public void sendMessageToServer(AbstractMessage message) {
		if (client != null) {
			client.send(message);
		}
		
	}

	public boolean isConnected() {
		return client != null;
	}


	
	
}
