package fr.lyrgard.hexScape.gui.desktop.message;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.model.game.Game;

public class GameSelectedMessage extends AbstractMessage {

	private Game game;

	public GameSelectedMessage(Game game) {
		super();
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
	
	
}
