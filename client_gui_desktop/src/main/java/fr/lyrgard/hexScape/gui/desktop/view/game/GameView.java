package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.ArmiesTabbedPane;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.ArmyCardPanel;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedCardPanel;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedPiecePanel;
import fr.lyrgard.hexScape.gui.desktop.components.game.View3d;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class GameView extends AbstractView {

	private static final long serialVersionUID = 2076159331208010791L;

	public GameView(final View3d view3d) {
		setLayout(new BorderLayout());
		final JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
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
		DisplayMapMessage displayMap = new DisplayMapMessage(gameId);
		CoreMessageBus.post(displayMap);
		HexScapeFrame.getInstance().showView(ViewEnum.GAME);
	}

	@Override
	public void refresh() {
		add(HexScapeFrame.getInstance().getView3d(), BorderLayout.CENTER);
	}

}
