package fr.lyrgard.hexScape.gui.desktop.controller.online;

import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;

public class ChoosePlayerButtonController extends AbstractImageButtonController {
	
	public static final String PLAYER_ID = "playerId";
	public static final String GAME_ID = "gameId";
	
	@Override
	public void onClick() {
		String playerId = attributes.get(PLAYER_ID);
	
		OnlineScreenController screenController = (OnlineScreenController)screen.getScreenController();
		screenController.joinSelectedGameAsPlayer(playerId);
	}
}
