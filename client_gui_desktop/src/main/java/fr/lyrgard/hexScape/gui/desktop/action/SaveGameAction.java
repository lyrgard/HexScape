package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;











import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class SaveGameAction extends AbstractAction {

	private static final long serialVersionUID = -2241823896873941912L;

	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/chooseMap.png"));
	
	private Component parent;
	
	public SaveGameAction() {
		super("Save game", icon);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Game game = CurrentUserInfo.getInstance().getGame();

		if (game != null) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Game save file", "hsg");
			chooser.setFileFilter(filter);
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);

			int returnVal = chooser.showSaveDialog(parent);
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
	}
	
}
