package fr.lyrgard.hexScape.server;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;

import fr.lyrgard.hexScape.model.ServerConstant;
import fr.lyrgard.hexScape.model.message.RegisterMessageHelper;
import fr.lyrgard.hexScape.model.message.client.handshake.UserInformationMessage;
import fr.lyrgard.hexScape.model.message.client.room.GetRoomQueryMessage;
import fr.lyrgard.hexScape.model.message.common.room.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.model.message.server.handshake.UserIdMessage;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.server.listener.RoomMessageListener;
import fr.lyrgard.hexScape.server.listener.HandShakeListener;

public class HexScapeServerJme3Application extends SimpleApplication implements ConnectionListener {
	
	private static final Logger LOG = Logger.getLogger(HexScapeServerJme3Application.class.getCanonicalName());
	
	private Server server;
	
	@Override
	public void start() {
		super.start(JmeContext.Type.Headless);
	}
	
	
	@Override
	public void simpleInitApp() {
		server = null;
		try {
			server = Network.createServer(ServerConstant.SERVER_PORT);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to create the server network", e);
		}
		server.start();
		
		RegisterMessageHelper.registerMessages();
		server.addConnectionListener(this);
		server.addMessageListener(new RoomMessageListener(), GetRoomQueryMessage.class);
		server.addMessageListener(new RoomMessageListener(), RoomMessagePostedMessage.class);
		server.addMessageListener(new HandShakeListener(), UserInformationMessage.class);
	}


	@Override
	public void connectionAdded(Server server, HostedConnection client) {
		Map<String, HostedConnection> connectionByPlayerIds = HexScapeServer.getInstance().getConnectionByPlayerIds();
		String id = null;
		
		// generate a new player id
		while (connectionByPlayerIds.keySet().contains(id = HexScapeServer.getInstance().getIdService().getNewPlayerId())) {
		}
		
		Player player = new Player();
		player.setId(id);
		
		client.setAttribute(Player.class.getCanonicalName(), player);
		HexScapeServer.getInstance().getConnectionByPlayerIds().put(id, client);
		
		UserIdMessage message = new UserIdMessage();
		message.setUserId(id);
		client.send(message);
	}


	@Override
	public void connectionRemoved(Server server, HostedConnection client) {
		Player player = client.getAttribute(Player.class.getCanonicalName());
		HexScapeServer.getInstance().getRoomService().leaveRoom(player);
		
		HexScapeServer.getInstance().getConnectionByPlayerIds().remove(player.getId());
	}


	public Server getServer() {
		return server;
	}


	
	
	

}
