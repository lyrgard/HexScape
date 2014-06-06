package fr.lyrgard.hexScape.test.json;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.json.MessageJsonMapper;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;

public class MessageJsonMapperTest {

	private MessageJsonMapper mapper = MessageJsonMapper.getInstance();
	
	@Test
	public void testToJson() {
		MessagePostedMessage message = new MessagePostedMessage("1", "chat message", "roomId", "gameId");
		
		String result = null;
		try {
			result = mapper.toJson(message);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(result);
		assertEquals("{\"type\":\"MessagePostedMessage\",\"playerId\":\"1\",\"message\":\"chat message\",\"roomId\":\"roomId\",\"gameId\":\"gameId\"}", result);
	}
	
	@Test
	public void testFromJson() {
		AbstractMessage m = null;
		try {
			m = mapper.fromJson("{\"type\":\"MessagePostedMessage\",\"playerId\":\"1\",\"message\":\"chat message\",\"roomId\":\"roomId\",\"gameId\":\"gameId\"}");
		} catch (JsonParseException e) {
			fail(e.getMessage());
		} catch (JsonMappingException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(m);
	}
	
	private final static String ARMY_LOADED_MESSAGE_JSON = "{\"type\":\"ArmyLoadedMessage\",\"playerId\":\"playerId\",\"army\":{\"name\":\"armyName\",\"cardsById\":{\"cardInstance1\":{\"id\":\"cardInstance1\",\"cardTypeId\":\"cardTypeId1\",\"number\":1,\"markers\":[{\"type\":\"MarkerInstance\",\"markerDefinitionId\":\"markerDefinition1\"},{\"type\":\"StackableMarkerInstance\",\"markerDefinitionId\":\"markerDefinition2\",\"number\":2}]},\"cardInstance2\":{\"id\":\"cardInstance2\",\"cardTypeId\":\"cardTypeId2\",\"number\":2,\"markers\":[]}}}}";
	
	@Test
	public void testToJsonArmyLoaded() {
		MarkerInstance marker1 = new MarkerInstance("markerDefinition1");
		StackableMarkerInstance marker2 = new StackableMarkerInstance("markerDefinition2", 2);
		CardInstance cardInstance1 = new CardInstance("cardInstance1", "cardTypeId1", 1);
		cardInstance1.getMarkers().add(marker1);
		cardInstance1.getMarkers().add(marker2);
		CardInstance cardInstance2 = new CardInstance("cardInstance2", "cardTypeId2", 2);
		
		Army army = new Army();
		army.setName("armyName");
		army.getCardsById().put(cardInstance1.getId(), cardInstance1);
		army.getCardsById().put(cardInstance2.getId(), cardInstance2);
		ArmyLoadedMessage message = new ArmyLoadedMessage("playerId", army);
		
		String result = null;
		try {
			result = mapper.toJson(message);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(result);
		assertEquals(ARMY_LOADED_MESSAGE_JSON, result);
	}
	
	@Test
	public void testFromJsonArmyLoaded() {
		AbstractMessage m = null;
		try {
			m = mapper.fromJson(ARMY_LOADED_MESSAGE_JSON);
		} catch (JsonParseException e) {
			fail(e.getMessage());
		} catch (JsonMappingException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(m);
	}
}
