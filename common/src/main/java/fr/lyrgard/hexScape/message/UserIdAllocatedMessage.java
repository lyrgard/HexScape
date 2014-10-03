package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserIdAllocatedMessage extends AbstractUserMessage {

	@JsonCreator
	public UserIdAllocatedMessage(@JsonProperty("userId") String userId) {
		super(userId);
	}

}
