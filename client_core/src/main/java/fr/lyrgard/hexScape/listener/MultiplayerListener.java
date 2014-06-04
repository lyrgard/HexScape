package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;

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
