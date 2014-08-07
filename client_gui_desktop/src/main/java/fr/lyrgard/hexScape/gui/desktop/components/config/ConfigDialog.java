package fr.lyrgard.hexScape.gui.desktop.components.config;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;


public class ConfigDialog  extends JDialog {

	private static final long serialVersionUID = 1619661196299179236L;
	
	public ConfigDialog(Component parent) {
		setLocationRelativeTo(parent);
		setTitle("Configuration");
		
		Dimension dim = new Dimension(600, 400);
		setPreferredSize(dim);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Parameters", null, new ConfigPanel(), null);
		tabbedPane.addTab("Assets", null, new AssetLoaderPanel(), null);
		
		add(tabbedPane);
		
		pack();
	}
}
