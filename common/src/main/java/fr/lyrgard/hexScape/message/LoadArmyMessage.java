package fr.lyrgard.hexScape.message;

import java.io.File;

public class LoadArmyMessage extends AbstractMessage {
	
	private File armyFile;
	
	

	public LoadArmyMessage(String playerId, File armyFile) {
		super(playerId);
		this.armyFile = armyFile;
	}



	public File getArmyFile() {
		return armyFile;
	}

}
