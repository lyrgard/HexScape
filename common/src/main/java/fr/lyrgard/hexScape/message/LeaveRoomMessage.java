package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaveRoomMessage extends AbstractPlayerMessage {

	@JsonCreator
	public LeaveRoomMessage(@JsonProperty("playerId") String playerId) {
		super(playerId);
	}
}
