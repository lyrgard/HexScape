package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DisconnectedFromServerMessage extends AbstractUserMessage {

	@JsonCreator
	public DisconnectedFromServerMessage(@JsonProperty("userId") String userId) {
		super(userId);
	}

}
