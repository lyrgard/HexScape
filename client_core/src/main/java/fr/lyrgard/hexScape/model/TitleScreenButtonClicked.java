package fr.lyrgard.hexScape.model;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.TitleScreenSprite.Type;

public class TitleScreenButtonClicked extends AbstractMessage {

	private Type type;

	public TitleScreenButtonClicked(Type type) {
		super();
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	
}
