package fr.lyrgard.hexScape.message;

import java.io.File;

public class LoadMapMessage extends AbstractMessage {

	private File mapFile;

	
	public LoadMapMessage(String playerId, File mapFile) {
		super(playerId);
		this.mapFile = mapFile;
	}


	public File getMapFile() {
		return mapFile;
	}
}
