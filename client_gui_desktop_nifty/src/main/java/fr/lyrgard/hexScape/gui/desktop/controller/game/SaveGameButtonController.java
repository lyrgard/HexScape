package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class SaveGameButtonController extends AbstractImageButtonController {

	@Override
	public void onClick() {
		final Game game = CurrentUserInfo.getInstance().getGame();

		if (game != null) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"Game save file", "hsg");
					chooser.setFileFilter(filter);
					chooser.setDialogType(JFileChooser.SAVE_DIALOG);

					int returnVal = chooser.showSaveDialog(HexScapeFrame.getInstance());
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						File saveFile = chooser.getSelectedFile();
						if (!saveFile.getName().endsWith(".hsg")) {
							saveFile = new File(saveFile.getAbsolutePath() + ".hsg");
						}

						try {
							// clone the game for removing hidden markers, and user ids
							Game clone = Game.fromJson(game.toJson());
							clone.setId(null);
							for (Player player : clone.getPlayers()) {
								player.setUserId(null);
								if (player.getArmy() != null) {
									for (CardInstance card : player.getArmy().getCards()) {
										Iterator<MarkerInstance> it = card.getMarkers().iterator();
										while (it.hasNext()) {
											MarkerInstance marker = it.next();
											if (marker instanceof HiddenMarkerInstance) {
												it.remove();
											}
										}
									}
								}
							}
							String content = clone.toJson();
							FileUtils.writeStringToFile(saveFile, content, StandardCharsets.UTF_8);
						} catch (IOException ex) {
							GuiMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getId(), "Error when trying to save the game"));
						}
					}
				}
			});
		}	
	}
}