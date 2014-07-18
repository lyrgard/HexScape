package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.gui.desktop.components.config.ConfigDialog;

public class OpenConfigDialogAction extends AbstractAction {

	private static final long serialVersionUID = -5802919708030860729L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/config.png"));

	private Component parent;
	
	public OpenConfigDialogAction(Component parent) {
		super("Open configuration", icon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigDialog configDialog = new ConfigDialog(parent);
		configDialog.setVisible(true);
	}
}
