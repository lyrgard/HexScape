package fr.lyrgard.hexScape.model.marker;

import java.util.ArrayList;
import java.util.Collection;

public class HiddenMarkerDefinition extends MarkerDefinition {

	private Collection<RevealableMarkerDefinition> possibleMarkersHidden = new ArrayList<>();

	public Collection<RevealableMarkerDefinition> getPossibleMarkersHidden() {
		return possibleMarkersHidden;
	}
	
	
}
