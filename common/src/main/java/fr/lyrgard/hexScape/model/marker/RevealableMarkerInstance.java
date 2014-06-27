package fr.lyrgard.hexScape.model.marker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RevealableMarkerInstance extends MarkerInstance {

	@JsonCreator
	public RevealableMarkerInstance(@JsonProperty("markerDefinitionId") String markerDefinitionId) {
		super(markerDefinitionId);
	}
	
}
