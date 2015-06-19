package fr.lyrgard.hexScape.gui.desktop.controller.game;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.ElementInteraction;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NonBlockingElementInteraction extends ElementInteraction {

	public NonBlockingElementInteraction(Nifty niftyParam, Element element) {
		super(niftyParam, element);
	}

	@Override
	public boolean process(NiftyMouseInputEvent mouseEvent, long eventTime,	boolean mouseInside, boolean canHandleInteraction,boolean hasMouseAccess) {
		super.process(mouseEvent, eventTime, mouseInside, canHandleInteraction,	hasMouseAccess);
		return false;
	}	
}
