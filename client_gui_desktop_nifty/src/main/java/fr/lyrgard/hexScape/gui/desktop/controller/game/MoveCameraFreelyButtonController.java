package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.LookFreelyMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class MoveCameraFreelyButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		CoreMessageBus.post(new LookFreelyMessage(CurrentUserInfo.getInstance().getPlayerId()));	
	}
}