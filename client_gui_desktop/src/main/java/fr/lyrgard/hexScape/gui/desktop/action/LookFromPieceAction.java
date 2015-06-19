package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.LookFromSelectedPieceMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class LookFromPieceAction extends AbstractAction {


	private static final long serialVersionUID = -178073398307006045L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/lookAtPointOfView.png"));
	
	private String pieceId;
	

	public LookFromPieceAction(String pieceId) {
		super("Point of view", icon);
		this.pieceId = pieceId;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		CoreMessageBus.post(new LookFromSelectedPieceMessage(CurrentUserInfo.getInstance().getPlayerId()));		
	}
}
