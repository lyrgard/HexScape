package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class CancelButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		HexScapeCore.getInstance().getHexScapeJme3Application().displayTitleScreen();
		nifty.gotoScreen("homeScreen");
	}

}
