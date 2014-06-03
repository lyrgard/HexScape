package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PostMessageMessage;

public class ChatMessageLocalListener {

	private static ChatMessageLocalListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ChatMessageLocalListener();
			MessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			MessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private ChatMessageLocalListener() {
	}
	
	
	@Subscribe public void onPostMessageMessage(PostMessageMessage message) {
		// Just bounce back the message
		String playerId = message.getPlayerId();
		String gameId = message.getGameId();
		String roomId = message.getRoomId();
		String messageContent = message.getMessage();
		MessageBus.post(new MessagePostedMessage(playerId, messageContent, roomId, gameId));
	}
}
