package fr.lyrgard.hexScape.listener;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.game.GameMessagePostedEvent;
import fr.lyrgard.hexScape.event.room.RoomMessagePostedEvent;
import fr.lyrgard.hexScape.model.ChatTypeEnum;
import fr.lyrgard.hexScape.model.message.common.room.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

public class ChatService {

	public void postMessage(String message, ChatTypeEnum chatType) {
		Player player = HexScapeCore.getInstance().getPlayer();
		
		if (HexScapeCore.getInstance().getMultiplayerService().isConnected()) {
			// Send it to the server, and it will send it to us back
			if (chatType == ChatTypeEnum.GAME_CHAT) {
				if (player.getGame() != null) {
					
				}
			} else if (chatType == ChatTypeEnum.ROOM_CHAT) {
				if (player.getRoom() != null) {
					RoomMessagePostedMessage m = new RoomMessagePostedMessage();
					m.setPlayer(player);
					m.setMessage(message);
					m.setRoomId(Room.DEFAULT_ROOM_ID);
					HexScapeCore.getInstance().getMultiplayerService().sendMessageToServer(m);
				}
			}
		} else {
			if (chatType == ChatTypeEnum.GAME_CHAT) {
				GameMessagePostedEvent event = new GameMessagePostedEvent();
				event.setPlayer(player);
				event.setMessage(message);
				HexScapeCore.getInstance().getEventBus().post(event);
			} else if (chatType == ChatTypeEnum.ROOM_CHAT) {
				RoomMessagePostedEvent event = new RoomMessagePostedEvent();
				event.setPlayer(player);
				event.setMessage(message);
				HexScapeCore.getInstance().getEventBus().post(event);
			}
		}
	}
}
