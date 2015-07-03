package fr.lyrgard.hexScape.gui.desktop.controller.game;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class CardMenuController implements EventTopicSubscriber<MenuItemActivatedEvent<CardInstance>> {

	@Override
	public void onEvent(String id, MenuItemActivatedEvent<CardInstance> event) {
		
	}

	
}
