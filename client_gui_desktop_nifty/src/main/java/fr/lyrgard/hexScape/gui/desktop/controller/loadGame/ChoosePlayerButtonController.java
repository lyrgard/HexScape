package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class ChoosePlayerButtonController extends AbstractImageButtonController {
	
	public static final String PLAYER_ID = "playerId";
	public static final String GAME_ID = "gameId";
	
	@Override
	public void onClick() {
		String playerId = attributes.get(PLAYER_ID);
		String gameId = attributes.get(GAME_ID);
	
		JoinGameMessage joinGameMessage = new JoinGameMessage(CurrentUserInfo.getInstance().getId(), gameId, playerId);
		CoreMessageBus.post(joinGameMessage);
	}
}
