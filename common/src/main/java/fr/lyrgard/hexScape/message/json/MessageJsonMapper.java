package fr.lyrgard.hexScape.message.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.lyrgard.hexScape.message.AbstractMessage;

public class MessageJsonMapper {

	private static final MessageJsonMapper INSTANCE = new MessageJsonMapper();
	
	public static MessageJsonMapper getInstance() {
		return INSTANCE;
	}
	
	private ObjectMapper mapper;
	
	private MessageJsonMapper() {
		mapper = new ObjectMapper();
		mapper.registerModule(new MessageJacksonModule());
	}
	
	public AbstractMessage fromJson(String string) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(string, AbstractMessage.class);
	}
	
	public String toJson(AbstractMessage message) throws JsonProcessingException {
		return mapper.writeValueAsString(message);
	}
}
