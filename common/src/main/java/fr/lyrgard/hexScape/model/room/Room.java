package fr.lyrgard.hexScape.model.room;

import java.util.ArrayList;
import java.util.Collection;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.User;

public class Room {
	
	public static final String DEFAULT_ROOM_ID = "HEXSCAPE";

	private String id;
	
	private String name;
	
	private Collection<Game> games;
	
	private Collection<User> users;
	
	public Collection<User> getUsers() {
		if (users == null) {
			users = new ArrayList<>();
		}
		return users;
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
