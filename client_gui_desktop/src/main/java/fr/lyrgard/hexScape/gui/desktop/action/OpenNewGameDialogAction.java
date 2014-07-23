package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.gui.desktop.components.game.NewGameDialog;

public class OpenNewGameDialogAction extends AbstractAction {

	private static final long serialVersionUID = -6086863326964887467L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/addGame.png"));

	private boolean muliplayer;
	
	private Component parent;
	
	public OpenNewGameDialogAction(boolean muliplayer, Component parent) {
		super("Create new game", icon);
		this.muliplayer = muliplayer;
		this.parent = parent;
	}
	
	public void actionPerformed(ActionEvent e) {
		NewGameDialog newGameDialog = new NewGameDialog(muliplayer, parent);
		newGameDialog.setModal(true);
		newGameDialog.setVisible(true);
	}

}
