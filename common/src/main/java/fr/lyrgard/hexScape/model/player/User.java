package fr.lyrgard.hexScape.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.room.Room;

public class User {

	private String id;
	
	private String name;
	
	private ColorEnum color = ColorEnum.BLUE;
	
	@JsonIgnore
	private Room room;
	
	@JsonIgnore
	private String gameId;
	
	@JsonIgnore
	private Player player;
	
	@JsonIgnore
	public String getPlayerId() {
		if (player != null) {
			return player.getId();
		} else {
			return null;
		}
	}
	
	@JsonIgnore
	public String getGameId() {
		return gameId;
	}
	
	@JsonIgnore
	public String getRoomId() {
		if (room != null) {
			return room.getId();
		} else {
			return null;
		}
	}
	
	@JsonIgnore
	public boolean isPlayingGame() {
		Game game = getGame();
		if (game != null && player != null) {
			return game.getPlayers().contains(player);
		} else {
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColorEnum getColor() {
		return color;
	}

	public void setColor(ColorEnum color) {
		this.color = color;
	}

	@JsonIgnore
	public Game getGame() {
		if (gameId != null) {
			return Universe.getInstance().getGamesByGameIds().get(gameId);
		} else {
			return null;
		}
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
