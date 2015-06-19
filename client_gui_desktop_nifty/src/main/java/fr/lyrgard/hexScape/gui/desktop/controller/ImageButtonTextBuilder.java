package fr.lyrgard.hexScape.gui.desktop.controller;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ImageButtonTextBuilder extends ControlBuilder {

	public ImageButtonTextBuilder() {
		super("imageButtonText");
	}
	
	public ImageButtonTextBuilder(String id) {
		super(id, "imageButtonText");
	}
	
	public void clickAction(ImageButtonClickAction action) {
		//controller(new AbstractImageButtonController(action));
	}
}
