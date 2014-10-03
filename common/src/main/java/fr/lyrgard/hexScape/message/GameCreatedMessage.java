package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.game.Game;

public class GameCreatedMessage extends AbstractUserMessage {
	
	private Game game;
	
	@JsonCreator
	public GameCreatedMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("game") Game game) {
		super(userId);
		this.game = game;
	}

	
	
	public Game getGame() {
		return game;
	}

}
