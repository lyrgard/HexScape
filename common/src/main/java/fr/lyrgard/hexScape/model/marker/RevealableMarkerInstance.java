package fr.lyrgard.hexScape.model.marker;

public class RevealableMarkerInstance extends MarkerInstance {

	private boolean hidden;

	public RevealableMarkerInstance(String markerDefinitionId, boolean hidden) {
		super(markerDefinitionId);
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
}
