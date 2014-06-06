package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.ConnectToServerMessage;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public class ServerListener {

	private static ServerListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ServerListener();
			MessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			MessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private ServerListener() {
	}
	
	@Subscribe public void onConnectToServerMessage(ConnectToServerMessage message) {
		String host = message.getHost();
		int port = message.getPort();
		ClientNetwork.getInstance().connect(host, port);
	}
	
	@Subscribe public void onDisconnectFromServerMessage(DisconnectedFromServerMessage message) {
		// TODO
	}


	@Subscribe public void onUserIdAllocated(UserIdAllocatedMessage message) {
		String playerId = message.getPlayerId();
		HexScapeCore.getInstance().setPlayerId(playerId);
		ConnectedToServerMessage resultMessage = new ConnectedToServerMessage(playerId);
		MessageBus.post(resultMessage);
	}
	
	
}
