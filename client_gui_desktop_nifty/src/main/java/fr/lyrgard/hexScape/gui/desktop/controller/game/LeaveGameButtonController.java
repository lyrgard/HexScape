package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.LeaveGameMessage;

public class LeaveGameButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		LeaveGameMessage message = new LeaveGameMessage();
		CoreMessageBus.post(message);
	}
}