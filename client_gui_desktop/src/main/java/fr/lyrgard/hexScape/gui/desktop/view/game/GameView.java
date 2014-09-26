package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.Dimension;
import java.awt.EventQueue;

import net.miginfocom.swing.MigLayout;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.gui.desktop.view.common.View3d;
import fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel.LeftPanel;
import fr.lyrgard.hexScape.gui.desktop.view.game.rightPanel.RightPanel;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class GameView extends AbstractView {

	private static final long serialVersionUID = 2076159331208010791L;
	
	//private JButton leaveGameButton;

	private String currentGameId;
	
	public GameView(final View3d view3d) {
//		setLayout(new BorderLayout());
//		final JPanel leftPanel = new LeftPanel();
//		//leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
//		leftPanel.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
		
		this.setLayout(new MigLayout(
				"wrap", // Layout Constraints
				"[210:210:][][250:250:]", // Column constraints
				"[40:40:][]" // Row constraints
				));
		
		add(new LeftPanel(), "span 1 2, grow");
		add(new ActionMenu());
		add(new RightPanel(), "span 1 2, grow");
		
		
		
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();
		
		if (gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			this.currentGameId = gameId;
			DisplayMapMessage displayMap = new DisplayMapMessage(gameId, true);
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
		String gameId = message.getGameId();
		
		if (gameId.equals(currentGameId) && CurrentUserInfo.getInstance().getPlayer() == null) {
			// Current game && the player is null == the current user left the game
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
		add(view3d, "cell 1 1, grow, push");
	}

}
