package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserIdAllocatedMessage extends AbstractUserMessage {

	@JsonCreator
	public UserIdAllocatedMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}

}
