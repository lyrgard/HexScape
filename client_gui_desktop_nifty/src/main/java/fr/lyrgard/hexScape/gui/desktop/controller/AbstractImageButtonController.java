package fr.lyrgard.hexScape.gui.desktop.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public abstract class AbstractImageButtonController implements ImageButtonController {

	//private final static String IMAGE_ID = "#imageButtonImage";
	
	private final static String IMAGE_FILENAME = "image";
	private final static String IMAGE_HOVER_FILENAME = "imageHover";
	private final static String IMAGE_PRESSED_FILENAME = "imagePressed";
	
	protected Nifty nifty;
	protected Screen screen;
	protected Element element;
	protected Properties properties;
	protected Attributes attributes;
	
	

	protected NiftyImage image;
	protected NiftyImage imageHover;
	protected NiftyImage imagePressed;
	protected Element imageElement;
	
	public AbstractImageButtonController() {
	}

	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties properties, Attributes attributes) {
		this.nifty = nifty;
		this.screen = screen;
		this.element = element;
		this.properties = properties;
		this.attributes = attributes;
		
		//imageElement = element.findElementByName(element.getId() + IMAGE_ID);
		imageElement = element.getElements().get(0);
		
		String imageFilename = attributes.get(IMAGE_FILENAME);
		String imageHoverFilename = attributes.get(IMAGE_HOVER_FILENAME);
		String imagePressedFilename = attributes.get(IMAGE_PRESSED_FILENAME);
		
		if (imageFilename != null) {
			image = nifty.createImage(imageFilename, false);
		}
		
		if (imageHoverFilename != null) {
			imageHover = nifty.createImage(imageHoverFilename, false);
		}
		
		if (imagePressedFilename != null) {
			imagePressed = nifty.createImage(imagePressedFilename, false);
		}
		
		imageElement.getRenderer(ImageRenderer.class).setImage(image);
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
	
	public Attributes getAttributes() {
		return attributes;
	}
	
	public void onStartHover() {
		imageElement.getRenderer(ImageRenderer.class).setImage(imageHover);
	}
	
	public void onStopHover() {
		ImageRenderer renderer = imageElement.getRenderer(ImageRenderer.class);
		renderer.setImage(image);
	}
	
	public void doOnClick() {
		imageElement.getRenderer(ImageRenderer.class).setImage(imagePressed);
		onClick();
		imageElement.getRenderer(ImageRenderer.class).setImage(imageHover);
	}

	public abstract void onClick();
}
