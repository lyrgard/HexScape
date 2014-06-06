package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DisconnectFromServerMessage extends AbstractUserMessage {

	@JsonCreator
	public DisconnectFromServerMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}
