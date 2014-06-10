package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.room.Room;

public class RoomJoinedMessage extends AbstractMessage {
	
	private Room room;

	@JsonCreator
	public RoomJoinedMessage(@JsonProperty("room") Room room) {
		super();
		this.room = room;
	}

	public Room getRoom() {
		return room;
	}
	
	

}
