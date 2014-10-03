package fr.lyrgard.hexScape.gui.desktop.view.room;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;

public class ActivateOnlineService {

	private static ActivateOnlineService INSTANCE; 

	private Collection<Object> objectsToActivateOnline = new ArrayList<Object>();
	
	public static ActivateOnlineService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ActivateOnlineService();
		}
		return INSTANCE;
	}
	
	private ActivateOnlineService() {
		GuiMessageBus.register(this);
	}
	
	public void register(Object object) {
		objectsToActivateOnline.add(object);
	}
	
	
	@Subscribe public void onConnectedToServer(ConnectedToServerMessage message) {
		for (Object object : objectsToActivateOnline) {
			GuiMessageBus.register(object);
		}
	}
	
	@Subscribe public void onDisconnectedFromServer(DisconnectedFromServerMessage message) {
		for (Object object : objectsToActivateOnline) {
			GuiMessageBus.unregister(object);
		}
	}
}
