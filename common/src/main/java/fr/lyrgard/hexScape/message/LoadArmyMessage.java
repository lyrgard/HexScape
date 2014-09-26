package fr.lyrgard.hexScape.message;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoadArmyMessage extends AbstractPlayerMessage {
	
	private File armyFile;
	
	
	@JsonCreator
	public LoadArmyMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("armyFile") File armyFile) {
		super(playerId);
		this.armyFile = armyFile;
	}



	public File getArmyFile() {
		return armyFile;
	}

}
