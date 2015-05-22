package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class NewGameButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		LoadGameScreenController screenController = (LoadGameScreenController)screen.getScreenController();
		screenController.openNewGamePopup();
	}

}
