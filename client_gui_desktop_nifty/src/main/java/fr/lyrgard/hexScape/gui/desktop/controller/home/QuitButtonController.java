package fr.lyrgard.hexScape.gui.desktop.controller.home;

import java.util.concurrent.Callable;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;

public class QuitButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		if (HexScapeCore.getInstance().isOnline()) {
			DisconnectFromServerMessage message = new DisconnectFromServerMessage();
    		CoreMessageBus.post(message);
		}
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeFrame.getInstance().dispose();
				HexScapeCore.getInstance().getHexScapeJme3Application().stop(true);
				
				
				return null;
			}
		});
	}

}
