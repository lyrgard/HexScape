package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.GameMessagePostedMessage;
import fr.lyrgard.hexScape.message.PostGameMessageMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.PostRoomMessageMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class ChatMessageListener extends AbstractMessageListener {

	private static ChatMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ChatMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private ChatMessageListener() {
	}
	
	
	@Subscribe public void onPostMessageMessage(PostRoomMessageMessage message) {
		// Just bounce back the message
		String userId = message.getUserId();
		String roomId = message.getRoomId();
		String messageContent = message.getMessage();
		sendMessage(new RoomMessagePostedMessage(userId, messageContent, roomId));
	}
	
	@Subscribe public void onMessagePosted(RoomMessagePostedMessage message) {
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onPostMessageMessage(PostGameMessageMessage message) {
		// Just bounce back the message
		String messageContent = message.getMessage();
		sendMessage(new GameMessagePostedMessage(CurrentUserInfo.getInstance().getPlayerId(), messageContent));
	}
	
	@Subscribe public void onMessagePosted(GameMessagePostedMessage message) {
		GuiMessageBus.post(message);
	}
}
