package fr.lyrgard.hexScape.gui.desktop.controller;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ImageButtonBuilder extends ControlBuilder {

	public ImageButtonBuilder() {
		super("imageButton");
	}
	
	public ImageButtonBuilder(String id) {
		super(id, "imageButton");
	}
	
	public void clickAction(ImageButtonClickAction action) {
		//controller(new AbstractImageButtonController(action));
	}
}
