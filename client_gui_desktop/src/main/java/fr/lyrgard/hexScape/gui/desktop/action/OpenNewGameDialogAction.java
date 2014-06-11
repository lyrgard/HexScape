package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.gui.desktop.components.game.NewGameDialog;

public class OpenNewGameDialogAction extends AbstractAction {

	private static final long serialVersionUID = -6086863326964887467L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/addGame.png"));

	private boolean muliplayer;
	
	public OpenNewGameDialogAction(boolean muliplayer) {
		super("Start new game", icon);
		this.muliplayer = muliplayer;
	}
	
	public void actionPerformed(ActionEvent e) {
		NewGameDialog newGameDialog = new NewGameDialog(muliplayer);
		newGameDialog.setVisible(true);
	}

}
