package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.JoinGameMessage;

public class JoinGameAction extends AbstractAction {

	private static final long serialVersionUID = -8793227823204727607L;

	private String gameId;
	
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/connect.png"));
	
	public JoinGameAction(String gameId) {
		super("Join game", icon);
		this.gameId = gameId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JoinGameMessage message = new JoinGameMessage(HexScapeCore.getInstance().getPlayerId(), gameId);
		CoreMessageBus.post(message);
	}
}
