package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.components.game.View3d;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;

public class GameView extends AbstractView {

	private static final long serialVersionUID = 2076159331208010791L;
	
	//private JButton leaveGameButton;

	public GameView(final View3d view3d) {
		setLayout(new BorderLayout());
		final JPanel leftPanel = new LeftPanel();
		//leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
		
		add(leftPanel, BorderLayout.LINE_START);

//		ArmiesTabbedPane armiesTabbedPane = new ArmiesTabbedPane();
		
//		leaveGameButton = new JButton();
//		//SelectedCardPanel selectedCardPanel = new SelectedCardPanel();
//		
//		
//		leftPanel.add(armiesTabbedPane);
//		leftPanel.add(new SelectedCardPanel());
//		//leftPanel.add(new SelectedPiecePanel());
//		leftPanel.add(leaveGameButton);

		add(new RightPanel(), BorderLayout.LINE_END);
		
		
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
					//leaveGameButton.setAction(new LeaveGameAction(gameId));
					HexScapeFrame.getInstance().showView(ViewEnum.GAME);
				}
			});
		}
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		String playerId = message.getPlayerId();
		
		if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					if (HexScapeCore.getInstance().isOnline()) {
						HexScapeFrame.getInstance().showView(ViewEnum.ROOM);
					} else {
						HexScapeFrame.getInstance().showView(ViewEnum.HOME);
					}
				}
			});
		}
	}
	

	@Override
	public void refresh() {
		View3d view3d = HexScapeFrame.getInstance().getView3d();
		view3d.setPreferredSize(new Dimension(UNDEFINED_CONDITION, UNDEFINED_CONDITION));
		add(view3d, BorderLayout.CENTER);
	}

}
