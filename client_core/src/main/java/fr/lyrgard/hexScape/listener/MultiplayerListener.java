package fr.lyrgard.hexScape.listener;

import java.io.IOException;

import com.google.common.eventbus.Subscribe;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Network;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.event.ErrorEvent;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.model.message.RegisterMessageHelper;
import fr.lyrgard.hexScape.model.message.client.room.GetRoomQueryMessage;
import fr.lyrgard.hexScape.model.message.common.room.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.model.message.server.handshake.HandShakeCompleteMessage;
import fr.lyrgard.hexScape.model.message.server.handshake.UserIdMessage;
import fr.lyrgard.hexScape.model.message.server.room.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.model.message.server.room.PlayerLeftRoomMessage;
import fr.lyrgard.hexScape.model.message.server.room.RoomContentResponseMessage;

public class MultiplayerListener {

	private static MultiplayerListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new MultiplayerListener();
			MessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			MessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private MultiplayerListener() {
	}
	
	@Subscribe public void onConnectToServerMessage(ConnectedToServerMessage message) {
		// TODO
	}
	
	@Subscribe public void onDisconnectFromServerMessage(DisconnectedFromServerMessage message) {
		// TODO
	}



	
	
}
