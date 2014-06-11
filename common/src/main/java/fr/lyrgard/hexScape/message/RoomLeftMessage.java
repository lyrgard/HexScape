package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomLeftMessage extends AbstractUserMessage {

	@JsonCreator
	public RoomLeftMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}
