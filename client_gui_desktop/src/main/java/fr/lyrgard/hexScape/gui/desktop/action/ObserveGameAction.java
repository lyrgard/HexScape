package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ObserveGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class ObserveGameAction  extends AbstractAction {

	private static final long serialVersionUID = 5063094047335096760L;

	private String gameId;
	
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/lookAtPointOfView.png"));
	
	public ObserveGameAction(String gameId) {
		super("Observe game", icon);
		this.gameId = gameId;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ObserveGameMessage message = new ObserveGameMessage(CurrentUserInfo.getInstance().getId(), gameId);
		CoreMessageBus.post(message);
	}
	

}
