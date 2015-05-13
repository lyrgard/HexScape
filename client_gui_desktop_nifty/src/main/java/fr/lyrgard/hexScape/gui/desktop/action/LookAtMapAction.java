package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.LookFromAboveMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class LookAtMapAction extends AbstractAction {


	private static final long serialVersionUID = -178073398307006045L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/chooseMap.png"));
	

	public LookAtMapAction() {
		super("Map view", icon);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		CoreMessageBus.post(new LookFromAboveMessage(CurrentUserInfo.getInstance().getPlayerId()));		
	}
}
