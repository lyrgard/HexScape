package fr.lyrgard.hexScape.model.marker;

import java.io.File;

public class RevealableMarkerDefinition extends MarkerDefinition {

	private boolean hidden;
	
	/** Name of the marker for a player that can't see the marker value */ 
	private String hiddenName;
	
	/** image of the hidden marker for a player that can't see the value */
	private File notOwnerHiddenMarkerImage;
	
	/** image of the hidden marker for a player that can see the value */
	private File ownerHiddenMarkerImage;

	public String getHiddenName() {
		return hiddenName;
	}

	public void setHiddenName(String hiddenName) {
		this.hiddenName = hiddenName;
	}

	public File getNotOwnerHiddenMarkerImage() {
		return notOwnerHiddenMarkerImage;
	}

	public void setNotOwnerHiddenMarkerImage(File notOwnerHiddenMarkerImage) {
		this.notOwnerHiddenMarkerImage = notOwnerHiddenMarkerImage;
	}

	public File getOwnerHiddenMarkerImage() {
		return ownerHiddenMarkerImage;
	}

	public void setOwnerHiddenMarkerImage(File ownerHiddenMarkerImage) {
		this.ownerHiddenMarkerImage = ownerHiddenMarkerImage;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
