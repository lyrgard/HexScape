package fr.lyrgard.hexScape.gui.desktop.controller.option;

import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class SaveOptionButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		OptionScreenController screenController = (OptionScreenController)screen.getScreenController();
		screenController.saveOption();
	}

}
