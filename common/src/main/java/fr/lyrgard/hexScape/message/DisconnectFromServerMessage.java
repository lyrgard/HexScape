package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DisconnectFromServerMessage extends AbstractMessage {

	@JsonCreator
	public DisconnectFromServerMessage() {
	}

}
