package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.lyrgard.hexScape.gui.desktop.action.ChooseGameAction;
import net.miginfocom.swing.MigLayout;

public class RestoreGamePanel extends JPanel {

	private static final long serialVersionUID = 1619661196299179236L;
	
	private JButton restoreGame = new JButton(new ChooseGameAction(this));
	
	public RestoreGamePanel(Component parent) {
		
		this.setLayout(new MigLayout(
				 "", // Layout Constraints
				 "[]", // Column constraints
				 "[]" // Row constraints
				));
		
		add(restoreGame);
	}
}
