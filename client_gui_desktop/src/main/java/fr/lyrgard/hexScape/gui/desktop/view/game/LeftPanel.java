package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.LeaveGameAction;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.ArmiesTabbedPane;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedCardPanel;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
import net.miginfocom.swing.MigLayout;

public class LeftPanel extends JPanel {


	private static final long serialVersionUID = -4777269272432192620L;
	
	private JButton leaveGameButton = new JButton();
	
	public LeftPanel() {
		this.setLayout(new MigLayout(
				"wrap", // Layout Constraints
				"[center, 200]", // Column constraints
				"[grow]20[200]20[]" // Row constraints
				));
		
		add(new ArmiesTabbedPane(), "grow");
		add(new SelectedCardPanel(), "width 250, height 350");
		add(leaveGameButton);
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(HexScapeCore.getInstance().getPlayerId());
		
		if (player != null && player.getGameId() != null && gameId.equals(player.getGameId())) {
		
			DisplayMapMessage displayMap = new DisplayMapMessage(gameId);
			CoreMessageBus.post(displayMap);
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					leaveGameButton.setAction(new LeaveGameAction(gameId));
				}
			});
		}
	}
}
