package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.game.Game;

public class RestoreGameMessage extends AbstractMessage {

	private Game game;

	@JsonCreator
	public RestoreGameMessage(@JsonProperty("game") Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}
