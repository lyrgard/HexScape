package fr.lyrgard.hexScape.model.marker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StackableMarkerInstance extends MarkerInstance {

	private int number;

	@JsonCreator
	public StackableMarkerInstance(
			@JsonProperty("markerDefinitionId") String markerDefinitionId, 
			@JsonProperty("number") int number) {
		super(markerDefinitionId);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
