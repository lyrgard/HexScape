package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.game.Game;

public class GameJoinedMessage extends AbstractUserMessage {

	private Game game;
	
	private String playerId;

	@JsonCreator
	public GameJoinedMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("game") Game game,
			@JsonProperty("playerId") String playerId) {
		super(userId);
		this.game = game;
		this.playerId = playerId;
	}

	public Game getGame() {
		return game;
	}

	public String getPlayerId() {
		return playerId;
	}

	
	
}