package fr.lyrgard.hexScape.gui.desktop.components;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.lyrgard.hexScape.gui.desktop.components.chatComponent.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.components.diceComponent.DiceTabbedPane;

public class LeftPanel extends JPanel {
	
	private static final long serialVersionUID = -8890928527786697273L;

	public LeftPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(new ChatPanel());
		add(new DiceTabbedPane());
	}
}
