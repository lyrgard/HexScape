package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.RestoreGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class RestoreGameButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Game save file", "hsg");
				chooser.setFileFilter(filter);
				String lastOpenedDir = ConfigurationService.getInstance().getLocationSavedGames();
				if (lastOpenedDir != null) {
					chooser.setCurrentDirectory(new File(lastOpenedDir));
				}

				int returnVal = chooser.showOpenDialog(HexScapeFrame.getInstance());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					final File savedGameFile = chooser.getSelectedFile();

					String content;
					Game game = null;
					try {
						content = FileUtils.readFileToString(savedGameFile, StandardCharsets.UTF_8);
						game = Game.fromJson(content);

						ConfigurationService.getInstance().setLocationSavedGames(savedGameFile.getParentFile());
						ConfigurationService.getInstance().save();

						RestoreGameMessage message = new RestoreGameMessage(game);
						CoreMessageBus.post(message);		
					} catch (IOException e) {
						GuiMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getId(), "Error when trying to open the game save : " + e.getMessage()));
					}
				}
			}
		});
	}
}
