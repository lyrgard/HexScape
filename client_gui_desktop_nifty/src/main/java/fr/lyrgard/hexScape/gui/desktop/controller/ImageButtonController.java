package fr.lyrgard.hexScape.gui.desktop.controller;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryReleaseEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ImageButtonController implements Controller {
	
	private static final String IMAGE_ELEMENT_ID = "image";
	private static final String IMAGE_FILENAME = "image";
	private static final String IMAGE_HOVER_FILENAME = "imageHover";
	private static final String IMAGE_PRESSED_FILENAME = "imagePressed";

	protected Nifty nifty;
	protected Screen screen;
	protected Element element;
	protected Properties properties;
	protected Attributes attributes;
	
	protected Element imageElement;
	
	protected NiftyImage image;
	protected NiftyImage imageHover;
	protected NiftyImage imagePressed;
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties properties, Attributes attributes) {
		this.nifty = nifty;
		this.screen = screen;
		this.element = element;
		this.properties = properties;
		this.attributes = attributes;
		
		imageElement = element.findElementByName(IMAGE_ELEMENT_ID);
		
		String imageFilename = attributes.get(IMAGE_FILENAME);
		if (StringUtils.isNotEmpty(imageFilename)) {
			image = nifty.getRenderEngine().createImage(screen, imageFilename, false);
			imageElement.getRenderer(ImageRenderer.class).setImage(image);
		}
		
		String imageHoverFilename = attributes.get(IMAGE_HOVER_FILENAME);
		if (StringUtils.isNotEmpty(imageFilename)) {
			imageHover = nifty.getRenderEngine().createImage(screen, imageHoverFilename, false);
		}
		
		String imagePressedFilename = attributes.get(IMAGE_PRESSED_FILENAME);
		if (StringUtils.isNotEmpty(imageFilename)) {
			imagePressed = nifty.getRenderEngine().createImage(screen, imagePressedFilename, false);
		}
		
	}

	@Override
	public void init(Properties properties, Attributes attributes) {
	}

	@Override
	public boolean inputEvent(NiftyInputEvent arg0) {
		return false;
	}

	@Override
	public void onFocus(boolean arg0) {
	}

	@Override
	public void onStartScreen() {
	}

	@NiftyEventSubscriber(id="image")
	public void onClick(String id, NiftyMousePrimaryClickedEvent event) {
		imageElement.getRenderer(ImageRenderer.class).setImage(imagePressed);
	}
	
	@NiftyEventSubscriber(id="image")
	public void onClickRelease(String id, NiftyMousePrimaryReleaseEvent event) {
		imageElement.getRenderer(ImageRenderer.class).setImage(image);
	}
}
