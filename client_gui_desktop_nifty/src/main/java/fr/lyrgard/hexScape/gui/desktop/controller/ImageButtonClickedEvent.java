package fr.lyrgard.hexScape.gui.desktop.controller;

import de.lessvoid.nifty.NiftyEvent;

public class ImageButtonClickedEvent implements NiftyEvent<Void> {
	private AbstractImageButtonController button;

	public ImageButtonClickedEvent(final AbstractImageButtonController button) {
		this.button = button;
	}

	public AbstractImageButtonController getButton() {
		return button;
	}
}