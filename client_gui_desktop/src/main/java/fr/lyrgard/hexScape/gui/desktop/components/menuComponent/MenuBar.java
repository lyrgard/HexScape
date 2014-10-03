package fr.lyrgard.hexScape.gui.desktop.components.menuComponent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.lyrgard.hexScape.gui.desktop.action.ChooseArmyAction;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseMapAction;

public class MenuBar extends JMenuBar {
	
	private static final long serialVersionUID = 7811007862435944748L;

	public MenuBar() {
		
		JMenu fileMenu = new JMenu("File");
		add(fileMenu);
		
		JMenuItem loadMapMenuItem = new JMenuItem(new ChooseMapAction(getTopLevelAncestor()));
		fileMenu.add(loadMapMenuItem);
		
	
	}
}
