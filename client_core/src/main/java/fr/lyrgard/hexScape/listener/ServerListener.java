package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ConnectToServerMessage;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
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
		String playerId = HexScapeCore.getInstance().getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null) {
			ClientNetwork.getInstance().connect(player, host);
		}
	}
	
	@Subscribe public void onDisconnectFromServerMessage(DisconnectFromServerMessage message) {
		ClientNetwork.getInstance().disconnect();
		
		String oldPlayerId = HexScapeCore.getInstance().getPlayerId();
		Player player = Universe.getInstance().getPlayersByIds().get(oldPlayerId);
		
		Universe.getInstance().getGamesByGameIds().clear();
		Universe.getInstance().getRoomsByRoomIds().clear();
		Universe.getInstance().getPlayersByIds().clear();
		
		String newPlayerId = "1";
		HexScapeCore.getInstance().setPlayerId(newPlayerId);
		player.setId(newPlayerId);
		Universe.getInstance().getPlayersByIds().put(newPlayerId, player);
		
		HexScapeCore.getInstance().setOnline(false);
		DisconnectedFromServerMessage resultMessage = new DisconnectedFromServerMessage(newPlayerId);
		GuiMessageBus.post(resultMessage);
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
		CoreMessageBus.post(resultMessage);
	}
	
	@Subscribe public void onConnectedToServer(ConnectedToServerMessage message) {
		String playerId = message.getPlayerId();

		HexScapeCore.getInstance().setOnline(true);
		GuiMessageBus.post(message);
		
		JoinRoomMessage resultMessage = new JoinRoomMessage(playerId, Room.DEFAULT_ROOM_ID); 
		ClientNetwork.getInstance().send(resultMessage);
	}
	
}
