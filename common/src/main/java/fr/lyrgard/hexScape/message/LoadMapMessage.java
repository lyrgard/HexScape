package fr.lyrgard.hexScape.message;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoadMapMessage extends AbstractUserMessage {

	private File mapFile;

	@JsonCreator
	public LoadMapMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("mapFile") File mapFile) {
		super(userId);
		this.mapFile = mapFile;
	}


	public File getMapFile() {
		return mapFile;
	}
}
