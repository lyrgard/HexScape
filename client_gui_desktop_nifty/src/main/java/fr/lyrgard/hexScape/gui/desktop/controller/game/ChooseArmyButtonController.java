package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.LoadArmyMessage;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class ChooseArmyButtonController extends AbstractImageButtonController {

	public static final String PLAYER_ID_PROPERTY = "playerId";

	@Override
	public void onClick() {
		final String playerId = attributes.get(PLAYER_ID_PROPERTY);

		if (StringUtils.isNotEmpty(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"HexScape Army", "hsa");
					chooser.setFileFilter(filter);
					String lastOpenedDir = ConfigurationService.getInstance().getLocationArmies();
					if (lastOpenedDir != null) {
						chooser.setCurrentDirectory(new File(lastOpenedDir));
					}


					int returnVal = chooser.showOpenDialog(HexScapeFrame.getInstance());
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						final File armyFile = chooser.getSelectedFile();

						ConfigurationService.getInstance().setLocationArmies(armyFile.getParentFile());
						ConfigurationService.getInstance().save();

						LoadArmyMessage message = new LoadArmyMessage(playerId, armyFile);
						CoreMessageBus.post(message);			
					}
				}
			});
		}
	}

}
