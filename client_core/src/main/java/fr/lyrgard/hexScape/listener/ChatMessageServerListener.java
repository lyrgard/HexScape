package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.PostMessageMessage;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public class ChatMessageServerListener {

	private static ChatMessageServerListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ChatMessageServerListener();
			MessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			MessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private ChatMessageServerListener() {
	}
	
	
	@Subscribe public void onPostMessageMessage(PostMessageMessage message) {
		ClientNetwork.getInstance().send(message);
	}
}
