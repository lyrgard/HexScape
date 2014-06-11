package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PostMessageMessage;

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
	
	
	@Subscribe public void onPostMessageMessage(PostMessageMessage message) {
		// Just bounce back the message
		String playerId = message.getPlayerId();
		String gameId = message.getGameId();
		String roomId = message.getRoomId();
		String messageContent = message.getMessage();
		sendMessage(new MessagePostedMessage(playerId, messageContent, roomId, gameId));
	}
	
	@Subscribe public void onMessagePosted(MessagePostedMessage message) {
		GuiMessageBus.post(message);
	}
}
