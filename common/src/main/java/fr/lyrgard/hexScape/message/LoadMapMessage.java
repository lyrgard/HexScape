package fr.lyrgard.hexScape.message;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoadMapMessage extends AbstractUserMessage {

	private File mapFile;

	@JsonCreator
	public LoadMapMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("mapFile") File mapFile) {
		super(playerId);
		this.mapFile = mapFile;
	}


	public File getMapFile() {
		return mapFile;
	}
}
