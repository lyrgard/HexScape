package fr.lyrgard.hexScape.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.json.MessageJacksonModule;
import fr.lyrgard.hexScape.model.map.Map;

public class GameRecord {

	private String name;
	
	private Map map;
	
	private List<String> playerIds = new ArrayList<>();
	
	private List<AbstractMessage> actions = new ArrayList<>();
	
	@JsonIgnore
	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.registerModule(new MessageJacksonModule());
	}
	
	
	public static GameRecord fromJson(String string) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(string, GameRecord.class);
	}
	
	public String toJson() throws JsonProcessingException {
		return mapper.writeValueAsString(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public List<String> getPlayerIds() {
		return playerIds;
	}

	public List<AbstractMessage> getActions() {
		return actions;
	}
	
	
}
