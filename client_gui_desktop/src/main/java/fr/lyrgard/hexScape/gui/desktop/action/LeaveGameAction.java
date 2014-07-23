package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.LeaveGameMessage;

public class LeaveGameAction extends AbstractAction {


	private static final long serialVersionUID = 2981217311404645630L;

	private String gameId;

	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/disconnect.png"));

	public LeaveGameAction(String gameId) {
		super("Leave game", icon);
		this.gameId = gameId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LeaveGameMessage message = new LeaveGameMessage(HexScapeCore.getInstance().getPlayerId(), gameId);
		CoreMessageBus.post(message);
	}
}