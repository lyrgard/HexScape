package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectedToServerMessage extends AbstractUserMessage {

	@JsonCreator
	public ConnectedToServerMessage(@JsonProperty("userId") String userId) {
		super(userId);
	}

}
