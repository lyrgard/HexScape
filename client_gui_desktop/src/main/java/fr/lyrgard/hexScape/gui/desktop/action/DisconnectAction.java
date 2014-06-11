package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;

public class DisconnectAction extends AbstractAction {

	private static final long serialVersionUID = -5721024919880931034L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/disconnect.png"));
	
	public DisconnectAction() {
		super("disconnect", icon);
	}
	
	public void actionPerformed(ActionEvent e) {
		String playerId = HexScapeCore.getInstance().getPlayerId();
		
		DisconnectFromServerMessage message = new DisconnectFromServerMessage(playerId);
		CoreMessageBus.post(message);
	}

}
