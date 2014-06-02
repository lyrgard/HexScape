package fr.lyrgard.hexScape.model.room;

import java.util.ArrayList;
import java.util.Collection;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class Room {
	
	public static final String DEFAULT_ROOM_ID = "HexScape";

	private String id;
	
	private String name;
	
	private Collection<Game> games;
	
	private Collection<Player> players;
	
	public Collection<Player> getPlayers() {
		if (players == null) {
			players = new ArrayList<>();
		}
		return players;
	}
	
	public Collection<Game> getGames() {
		if (games == null) {
			games = new ArrayList<>();
		}
		return games;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
