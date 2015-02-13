package fr.lyrgard.hexScape.service;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.GameRecord;

public class GameRecorderService {

	private static GameRecorderService INSTANCE;
	
	public static GameRecorderService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GameRecorderService();
		}
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
