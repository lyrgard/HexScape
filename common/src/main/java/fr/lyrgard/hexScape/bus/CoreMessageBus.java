package fr.lyrgard.hexScape.bus;

import com.google.common.eventbus.EventBus;

import fr.lyrgard.hexScape.message.AbstractMessage;

public class CoreMessageBus {

	private static EventBus eventBus = new EventBus();
	
	public static void post(AbstractMessage message) {
		eventBus.post(message);
	}
	
	public static void register(Object object) {
		eventBus.register(object);
	}
	
	public static void unregister(Object object) {
		eventBus.unregister(object);
	}
}
