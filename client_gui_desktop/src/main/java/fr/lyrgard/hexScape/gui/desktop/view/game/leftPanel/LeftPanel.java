package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.LeaveGameAction;
import fr.lyrgard.hexScape.gui.desktop.action.SaveGameAction;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import net.miginfocom.swing.MigLayout;

public class LeftPanel extends JPanel {


	private static final long serialVersionUID = -4777269272432192620L;
	
	private JButton leaveGameButton = new JButton(new LeaveGameAction());
	private JButton saveGameButton = new JButton(new SaveGameAction());
	
	public LeftPanel() {
		this.setLayout(new MigLayout(
				"wrap", // Layout Constraints
				"[center, 200]", // Column constraints
				"[grow]20[200]20[][]" // Row constraints
				));
		
		
		
		add(new ArmiesTabbedPane(), "grow");
		add(new SelectedCardPanel(), "width 250, height 350");
		add(saveGameButton);
		add(leaveGameButton);
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		
	}
}
