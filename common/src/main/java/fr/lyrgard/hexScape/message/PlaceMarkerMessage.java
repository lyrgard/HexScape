package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceMarkerMessage extends AbstractMarkerMessage {

	private String hiddenMarkerTypeId;
	
	private String markerTypeId;
	
	private int number;
	
	private boolean stackable;
	
	@JsonCreator
	public PlaceMarkerMessage(
			@JsonProperty("cardId") String cardId, 
			@JsonProperty("markerTypeId") String markerTypeId, 
			@JsonProperty("number") int number,
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId,
			@JsonProperty("stackable") boolean stackable) {
		super(cardId, null);
		this.markerTypeId = markerTypeId;
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
		this.number = number;
		this.stackable = stackable;
	}



	public String getMarkerTypeId() {
		return markerTypeId;
	}

	public int getNumber() {
		return number;
	}



	public String getHiddenMarkerTypeId() {
		return hiddenMarkerTypeId;
	}



	public void setHiddenMarkerTypeId(String hiddenMarkerTypeId) {
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}



	public boolean isStackable() {
		return stackable;
	}

	
}
