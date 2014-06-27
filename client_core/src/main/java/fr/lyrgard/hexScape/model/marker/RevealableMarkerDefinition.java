package fr.lyrgard.hexScape.model.marker;

public class RevealableMarkerDefinition extends MarkerDefinition {

	private boolean canBePlacedRevealed;
	
	private HiddenMarkerDefinition hiddenMarkerDefinition;

	public boolean canBePlacedRevealed() {
		return canBePlacedRevealed;
	}

	public void setCanBePlacedRevealed(boolean canBePlacedRevealed) {
		this.canBePlacedRevealed = canBePlacedRevealed;
	}

	public HiddenMarkerDefinition getHiddenMarkerDefinition() {
		return hiddenMarkerDefinition;
	}

	public void setHiddenMarkerDefinition(
			HiddenMarkerDefinition hiddenMarkerDefinition) {
		this.hiddenMarkerDefinition = hiddenMarkerDefinition;
	}
}
