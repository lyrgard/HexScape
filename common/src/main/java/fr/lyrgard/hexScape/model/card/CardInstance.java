package fr.lyrgard.hexScape.model.card;

import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.marker.MarkerInstance;

public class CardInstance {
	
	private String id;
	
	private String cardTypeId;
	
	private int number;
	
	private TreeSet<MarkerInstance> markers;

	@JsonCreator
	public CardInstance(
			@JsonProperty("id") String id, 
			@JsonProperty("cardTypeId") String cardTypeId, 
			@JsonProperty("number") int number) {
		super();
		this.id = id;
		this.cardTypeId = cardTypeId;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	

	public TreeSet<MarkerInstance> getMarkers() {
		if (markers == null) {
			markers = new TreeSet<>();
		}
		return markers;
	}

	public String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

}
