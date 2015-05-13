package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.gui.desktop.view.common.newGame.CreateGameDialog;

public class OpenNewGameDialogAction extends AbstractAction {

	private static final long serialVersionUID = -6086863326964887467L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/addGame.png"));
	
	private Component parent;
	
	public OpenNewGameDialogAction(Component parent) {
		super("Create new game", icon);
		this.parent = parent;
	}
	
	public void actionPerformed(ActionEvent e) {
		CreateGameDialog createGameDialog = new CreateGameDialog(parent);
		createGameDialog.setModal(true);
		createGameDialog.setVisible(true);
	}

}
