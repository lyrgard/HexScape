package fr.lyrgard.hexScape.gui.desktop.message;

import fr.lyrgard.hexScape.message.AbstractMessage;

public class ChangeDieTypeSelectedMessage extends AbstractMessage {

	private String newDieTypeId;

	public ChangeDieTypeSelectedMessage(String newDieTypeId) {
		super();
		this.newDieTypeId = newDieTypeId;
	}

	public String getNewDieTypeId() {
		return newDieTypeId;
	}
	
	
}
