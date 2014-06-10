package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.ConnectToServerMessage;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
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
		String playerId = HexScapeCore.getInstance().getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null) {
			ClientNetwork.getInstance().connect(player, host, port);
		}
	}
	
	@Subscribe public void onDisconnectFromServerMessage(DisconnectFromServerMessage message) {
		ClientNetwork.getInstance().disconnect();
		String newPlayerId = "1";
		HexScapeCore.getInstance().setPlayerId(newPlayerId);
		ChatMessageLocalListener.start();
		DisconnectedFromServerMessage resultMessage = new DisconnectedFromServerMessage(newPlayerId);
		MessageBus.post(resultMessage);
	}


	@Subscribe public void onUserIdAllocated(UserIdAllocatedMessage message) {
		String playerId = message.getPlayerId();
		String oldPlayerId = HexScapeCore.getInstance().getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(oldPlayerId);
		
		if (player != null) {
			player.setId(playerId);
			Universe.getInstance().getPlayersByIds().put(playerId, player);
		}
		HexScapeCore.getInstance().setPlayerId(playerId);
		
		if (player != null) {
			Universe.getInstance().getPlayersByIds().remove(oldPlayerId);
		}
		ConnectedToServerMessage resultMessage = new ConnectedToServerMessage(playerId);
		MessageBus.post(resultMessage);
	}
	
	@Subscribe public void onConnectedToServer(ConnectedToServerMessage message) {
		String playerId = message.getPlayerId();

		ChatMessageLocalListener.stop();
		ChatMessageServerListener.start();
		JoinRoomMessage resultMessage = new JoinRoomMessage(playerId, "hexscape"); 
		ClientNetwork.getInstance().send(resultMessage);
	}
	
}
