package fr.lyrgard.hexScape.gui.desktop;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.lyrgard.hexScape.gui.desktop.chat.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.dice.DiceTabbedPane;

public class LeftPanel extends JPanel {
	
	private static final long serialVersionUID = -8890928527786697273L;

	public LeftPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(new ChatPanel());
		add(new DiceTabbedPane());
	}
}
