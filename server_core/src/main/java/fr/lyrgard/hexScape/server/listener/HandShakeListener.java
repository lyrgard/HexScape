package fr.lyrgard.hexScape.server.listener;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import fr.lyrgard.hexScape.model.message.client.handshake.UserInformationMessage;
import fr.lyrgard.hexScape.model.message.server.handshake.HandShakeCompleteMessage;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.server.HexScapeServer;

public class HandShakeListener implements MessageListener<HostedConnection> {

	@Override
	public void messageReceived(HostedConnection client, Message message) {
		UserInformationMessage m = (UserInformationMessage)message;
		Player player = client.getAttribute(Player.class.getCanonicalName());
		if (player.getId().equals(m.getPlayer().getId())) {
			player.setName(m.getPlayer().getName());
			player.setColor(m.getPlayer().getColor());
			
			HexScapeServer.getInstance().getRoomService().joinRoom(Room.DEFAULT_ROOM_ID, player);

			HandShakeCompleteMessage returnMessage = new HandShakeCompleteMessage();
			client.send(returnMessage);
		}
		
	}

}
