package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.LoadMapMessage;
import fr.lyrgard.hexScape.message.RestoreGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class ChooseGameAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(ChooseGameAction.class.getResource("/gui/icons/open.png"));

	private Component parent;
	
	public ChooseGameAction(Component parent) {
		super("restore a game", icon);
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

