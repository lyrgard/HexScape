package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class StartGameAction extends AbstractAction {

	private static final long serialVersionUID = -8793227823204727607L;

	private String gameId;
	
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/start.png"));
	
	public StartGameAction(String gameId) {
		super("Start game", icon);
		this.gameId = gameId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StartGameMessage message = new StartGameMessage(CurrentUserInfo.getInstance().getPlayerId(), gameId);
		CoreMessageBus.post(message);
	}
}
