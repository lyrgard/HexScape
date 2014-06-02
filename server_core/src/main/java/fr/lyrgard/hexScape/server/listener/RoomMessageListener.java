package fr.lyrgard.hexScape.server.listener;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import fr.lyrgard.hexScape.model.message.client.room.GetRoomQueryMessage;
import fr.lyrgard.hexScape.model.message.common.room.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.server.HexScapeServer;

public class RoomMessageListener implements MessageListener<HostedConnection> {

	@Override
	public void messageReceived(HostedConnection client, Message message) {
		if (message instanceof GetRoomQueryMessage) {
			Player player = client.getAttribute(Player.class.getCanonicalName());
			GetRoomQueryMessage m = (GetRoomQueryMessage)message;
			String roomId = m.getRoomId();
			HexScapeServer.getInstance().getRoomService().sendRoomToPlayer(roomId, player);
		} else if (message instanceof RoomMessagePostedMessage) {
			Player player = client.getAttribute(Player.class.getCanonicalName());
			String messageContent = ((RoomMessagePostedMessage)message).getMessage();
			String roomId = ((RoomMessagePostedMessage)message).getRoomId();
			HexScapeServer.getInstance().getRoomService().broadcastRoomMessage(roomId, player, messageContent);
		}
	}

}
