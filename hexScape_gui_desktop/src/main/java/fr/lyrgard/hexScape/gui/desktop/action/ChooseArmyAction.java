package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.lyrgard.hexScape.HexScapeCore;

public class ChooseArmyAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/open.png"));

	private Component parent;

	public ChooseArmyAction(Component parent) {
		super("open an army", icon);
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Heroscape Army", "hsa");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			final File armyFile = chooser.getSelectedFile();

			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

				public Void call() throws Exception {
					HexScapeCore.getInstance().getCardService().loadArmy(armyFile);
					return null;
				}
			});		


			
		}
	}
}
