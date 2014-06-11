package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCreatedMessage extends AbstractUserMessage {
	
	private String gameId;
	
	private String name;
	
	private String mapName;
	
	private int playerNumber;
	
	private int playerNumberLeft;
	
	@JsonCreator
	public GameCreatedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("gameId") String gameId,
			@JsonProperty("name") String name, 
			@JsonProperty("mapName") String mapName,
			@JsonProperty("playerNumber") int playerNumber, 
			@JsonProperty("playerNumberLeft") int playerNumberLeft) {
		super(playerId);
		this.gameId = gameId;
		this.name = name;
		this.mapName = mapName;
		this.playerNumber = playerNumber;
		this.playerNumberLeft = playerNumberLeft;
	}

	public String getName() {
		return name;
	}

	public String getMapName() {
		return mapName;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public int getPlayerNumberLeft() {
		return playerNumberLeft;
	}

	public String getGameId() {
		return gameId;
	}

}
