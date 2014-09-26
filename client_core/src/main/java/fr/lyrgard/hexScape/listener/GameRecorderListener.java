package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.model.GameRecord;
import fr.lyrgard.hexScape.service.GameRecorderService;

public class GameRecorderListener {

private static GameRecorderListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new GameRecorderListener();
			CoreMessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			CoreMessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private GameRecorderListener() {
	}
	
	private GameRecord record = new GameRecord();
	
	@Subscribe public void onEvent(GameCreatedMessage message) {
		GameRecorderService.getInstance().addAction(new CreateGameMessage(message.getGame().getName(), message.getGame().getMap(), message.getGame().getPlayerNumber()));
	}
	
	@Subscribe public void onEvent(ArmyLoadedMessage message) {
		GameRecorderService.getInstance().addAction(message);
	}
	
	@Subscribe public void onEvent(PiecePlacedMessage message) {
		GameRecorderService.getInstance().addAction(message);
	}
	
	@Subscribe public void onEvent(PieceMovedMessage message) {
		GameRecorderService.getInstance().addAction(message);
	}

	public GameRecord getRecord() {
		return record;
	}
}
