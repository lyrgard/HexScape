package fr.lyrgard.hexScape.event.room;

import fr.lyrgard.hexScape.model.room.Room;

public class RoomContentReceivedEvent {

	private Room room;

	public Room getRoom() {
		return room;
	}

	public RoomContentReceivedEvent(Room room) {
		super();
		this.room = room;
	}

}
