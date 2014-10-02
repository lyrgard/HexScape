package fr.lyrgard.hexScape.model.room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.User;

public class Room {
	
	public static final String DEFAULT_ROOM_ID = "HEXSCAPE";

	private String id;
	
	private String name;
	
	private Collection<Game> games;
	
	private Collection<User> users;
	
	@JsonIgnore
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Room fromJson(String string) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(string, Room.class);
	}
	
	public String toJson() throws JsonProcessingException {
		return mapper.writeValueAsString(this);
	}
	
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
