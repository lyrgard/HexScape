package fr.lyrgard.hexScape.gui.desktop.controller.home;

import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class QuitButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		HexScapeFrame.getInstance().exit();
	}

}
