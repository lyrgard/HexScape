package fr.lyrgard.hexScape.service;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.GameRecord;
import fr.lyrgard.hexScape.model.game.Game;

public class GameRecorderService {

	private static final GameRecorderService INSTANCE = new GameRecorderService();
	
	public static GameRecorderService getInstance() {
		return INSTANCE;
	}
	
	private GameRecorderService() {
	}
	
	private GameRecord gameRecord = new GameRecord();

	public void addAction(AbstractMessage action) {
		gameRecord.getActions().add(action);
	}

	public GameRecord getGameRecord() {
		return gameRecord;
	}
}
