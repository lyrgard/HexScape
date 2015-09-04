package fr.lyrgard.hexScape.gui.desktop.controller.home;

import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class ConnectToServerButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		HomeScreenController screenController = (HomeScreenController)screen.getScreenController();
		screenController.connectToServer();
	}
}
