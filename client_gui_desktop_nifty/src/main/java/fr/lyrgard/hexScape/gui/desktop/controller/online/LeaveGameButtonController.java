package fr.lyrgard.hexScape.gui.desktop.controller.online;

import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class LeaveGameButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		OnlineScreenController screenController = (OnlineScreenController)screen.getScreenController();
		screenController.leaveSelectedGame();
	}
}
