package fr.lyrgard.hexScape.model.marker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HiddenMarkerInstance extends MarkerInstance {

	private String hiddenMarkerDefinitionId;
	
	@JsonCreator
	public HiddenMarkerInstance(
			@JsonProperty("markerDefinitionId") String markerDefinitionId,
			@JsonProperty("hiddenMarkerDefinitionId") String hiddenMarkerDefinitionId) { 
		super(markerDefinitionId);
		this.hiddenMarkerDefinitionId = hiddenMarkerDefinitionId;
	}

	public String getHiddenMarkerDefinitionId() {
		return hiddenMarkerDefinitionId;
	}
	

}
