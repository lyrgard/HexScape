package fr.lyrgard.hexScape.gui.desktop.controller;

import java.util.Properties;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public abstract class AbstractImageButtonController implements ImageButtonController {

	//private final static String IMAGE_ID = "#imageButtonImage";

	protected Nifty nifty;
	protected Screen screen;
	protected Element element;
	protected Properties properties;
	protected Attributes attributes;
	private boolean activated = true;

	public AbstractImageButtonController() {
	}

	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties properties, Attributes attributes) {
		this.nifty = nifty;
		this.screen = screen;
		this.element = element;
		this.properties = properties;
		this.attributes = attributes;
	}

	@Override
	public void init(Properties properties, Attributes attributes) {
		if (element.getId() != null) {

			EventTopicSubscriber<NiftyMousePrimaryClickedEvent> mouseClickedSubscriber = new EventTopicSubscriber<NiftyMousePrimaryClickedEvent>() {
				@Override
				public void onEvent(final String topic, final NiftyMousePrimaryClickedEvent data) {
					nifty.publishEvent(topic, new ImageButtonClickedEvent(AbstractImageButtonController.this));
				}
			};
			nifty.subscribe(screen, element.getId(), NiftyMousePrimaryClickedEvent.class, mouseClickedSubscriber);
		}
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
	
	public void activate() {
		if (!activated) {
			element.stopEffect(EffectEventId.onCustom);
		}
		activated = true;
	}
	
	public void desactivate() {
		activated = false;
		element.startEffect(EffectEventId.onCustom, null,"desactivate");
	}
	
	public void setVisible(boolean visible) {
		element.setVisible(visible);
	}

	public void doOnclick() {
		onClick();
	}
	
	public abstract void onClick();
}
