package fr.lyrgard.hexScape.model.marker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnknownTypeMarkerInstance extends MarkerInstance {
	
	private String hiddenMarkerTypeId;
	
	private int number;
	
	@JsonCreator
	public UnknownTypeMarkerInstance(
			@JsonProperty("id") String id, 
			@JsonProperty("markerDefinitionId") String markerDefinitionId, 
			@JsonProperty("hiddenMarkerTypeId") String hiddenMarkerTypeId, 
			@JsonProperty("number") int number) {
		super(markerDefinitionId);
		setId(id);
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
		this.number = number;
	}

	public String getHiddenMarkerTypeId() {
		return hiddenMarkerTypeId;
	}

	public void setHiddenMarkerTypeId(String hiddenMarkerTypeId) {
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
