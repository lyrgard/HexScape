package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.LookFromAboveMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class LookAtMapButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		CoreMessageBus.post(new LookFromAboveMessage(CurrentUserInfo.getInstance().getPlayerId()));	
	}
}
