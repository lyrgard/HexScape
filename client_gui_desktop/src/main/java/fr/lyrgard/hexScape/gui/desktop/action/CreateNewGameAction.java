package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.model.game.Game;

public class CreateNewGameAction extends AbstractAction {

	private static final long serialVersionUID = -9195650535210264707L;

	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/addGame.png"));

	private Game game;
	
	public CreateNewGameAction(Game game) {
		super("Create the game", icon);
		this.game = game;
	}
	
	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		
		CreateGameMessage message = new CreateGameMessage(playerId, game.getName(), game.getMap(), game.getPlayerNumber());
		CoreMessageBus.post(message);
	}
}
