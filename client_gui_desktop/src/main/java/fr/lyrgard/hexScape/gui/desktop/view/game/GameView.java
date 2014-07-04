package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.ArmiesTabbedPane;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedCardPanel;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedPiecePanel;
import fr.lyrgard.hexScape.gui.desktop.components.game.View3d;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;

public class GameView extends AbstractView {

	private static final long serialVersionUID = 2076159331208010791L;

	public GameView(final View3d view3d) {
		setLayout(new BorderLayout());
		final JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
		
		add(leftPanel, BorderLayout.LINE_START);

		ArmiesTabbedPane armiesTabbedPane = new ArmiesTabbedPane();
		
		leftPanel.add(armiesTabbedPane);
		leftPanel.add(new SelectedCardPanel());
		leftPanel.add(new SelectedPiecePanel());
		
		add(view3d, BorderLayout.CENTER);
		

		add(new RightPanel(), BorderLayout.LINE_END);
		
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		String gameId = message.getGameId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(HexScapeCore.getInstance().getPlayerId());
		
		if (player != null && player.getGame() != null && gameId.equals(player.getGame().getId())) {
		
			DisplayMapMessage displayMap = new DisplayMapMessage(gameId);
			CoreMessageBus.post(displayMap);
			HexScapeFrame.getInstance().showView(ViewEnum.GAME);
		}
	}

	@Override
	public void refresh() {
		View3d view3d = HexScapeFrame.getInstance().getView3d();
		view3d.setPreferredSize(new Dimension(UNDEFINED_CONDITION, UNDEFINED_CONDITION));
		add(view3d, BorderLayout.CENTER);
	}

}
