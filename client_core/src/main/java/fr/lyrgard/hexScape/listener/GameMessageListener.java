package fr.lyrgard.hexScape.listener;

import fr.lyrgard.hexScape.bus.CoreMessageBus;

public class GameMessageListener {

private static GameMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new GameMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private GameMessageListener() {
	}
	
	
}
