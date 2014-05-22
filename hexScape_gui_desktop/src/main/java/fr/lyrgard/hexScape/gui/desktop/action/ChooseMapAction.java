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
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;

public class ChooseMapAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/open.png"));

	private Component parent;

	public ChooseMapAction(Component parent) {
		super("open a map", icon);
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"VirtualScape maps", "hsc");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			final File mapFile = chooser.getSelectedFile();

			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

				public Void call() throws Exception {
					HexScapeCore.getInstance().getMapService().loadMap(mapFile);
//					HexScapeFrame.getInstance().getCanvas3d().requestFocus();
					return null;
				}
			});		


			
		}
	}
}

