package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;




import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.LoadMapMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class ChooseMapButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"VirtualScape maps", "hsc");
				chooser.setFileFilter(filter);
				String lastOpenedDir = ConfigurationService.getInstance().getLocationMaps();
				if (lastOpenedDir != null) {
					chooser.setCurrentDirectory(new File(lastOpenedDir));
				}

				int returnVal = chooser.showOpenDialog(HexScapeFrame.getInstance());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					final File mapFile = chooser.getSelectedFile();

					ConfigurationService.getInstance().setLocationMaps(mapFile.getParentFile());
					ConfigurationService.getInstance().save();

					LoadMapMessage message = new LoadMapMessage(CurrentUserInfo.getInstance().getId(), mapFile);
					CoreMessageBus.post(message);		
				}
			}
		});
	}

}
