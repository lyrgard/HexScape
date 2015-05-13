package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.LoadMapMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class ChooseMapAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/chooseMap.png"));

	private Component parent;

	public ChooseMapAction(Component parent) {
		super("open a map", icon);
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"VirtualScape maps", "hsc");
		chooser.setFileFilter(filter);
		String lastOpenedDir = ConfigurationService.getInstance().getLocationMaps();
		if (lastOpenedDir != null) {
			chooser.setCurrentDirectory(new File(lastOpenedDir));
		}

		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			final File mapFile = chooser.getSelectedFile();
			
			ConfigurationService.getInstance().setLocationMaps(mapFile.getParentFile());
			ConfigurationService.getInstance().save();
			
			LoadMapMessage message = new LoadMapMessage(CurrentUserInfo.getInstance().getId(), mapFile);
			CoreMessageBus.post(message);		
		}
	}
}

