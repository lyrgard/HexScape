package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.LoadArmyMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class ChooseArmyAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/chooseArmy.png"));

	private Component parent;
	
	private String playerId;

	public ChooseArmyAction(Component parent, String playerId) {
		super("open an army", icon);
		this.parent = parent;
		this.playerId = playerId;
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"HexScape Army", "hsa");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			final File armyFile = chooser.getSelectedFile();
			
			LoadArmyMessage message = new LoadArmyMessage(playerId, armyFile);
			CoreMessageBus.post(message);			
		}
	}
}
