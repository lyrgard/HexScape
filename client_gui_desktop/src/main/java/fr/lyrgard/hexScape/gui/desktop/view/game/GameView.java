package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.ArmyCardPanel;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedCardPanel;
import fr.lyrgard.hexScape.gui.desktop.components.cardComponent.SelectedPiecePanel;
import fr.lyrgard.hexScape.gui.desktop.jme3Swing.SwingContext;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class GameView extends AbstractView {

	private static final long serialVersionUID = 2076159331208010791L;

	private JPanel armyPanel;

	public GameView(final Canvas panel3d) {
		setLayout(new BorderLayout());
		final JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		add(leftPanel, BorderLayout.LINE_START);

		armyPanel = new JPanel();
		armyPanel.setLayout(new BoxLayout(armyPanel, BoxLayout.Y_AXIS));
		final JScrollPane scrollPane = new JScrollPane(armyPanel);
		scrollPane.setPreferredSize(new Dimension(180, 500));
		scrollPane.setMaximumSize(new Dimension(180, 500));
		leftPanel.add(scrollPane);
		leftPanel.add(new SelectedCardPanel());
		leftPanel.add(new SelectedPiecePanel());
		
		
		final JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(panel3d, BorderLayout.CENTER);
		centerPanel.setFocusable(true);
		centerPanel.requestFocusInWindow();
		
		final SwingContext ctx = (SwingContext)HexScapeCore.getInstance().getHexScapeJme3Application().getContext();
		ctx.setInputSource(centerPanel);
		
		add(centerPanel, BorderLayout.CENTER);
		

		add(new RightPanel(), BorderLayout.LINE_END);
		

		centerPanel.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				centerPanel.requestFocusInWindow();
			}
		});
		
		panel3d.transferFocusBackward();
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onArmyLoaded(final ArmyLoadedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Army army = message.getArmy();
				if (armyPanel != null) {
					armyPanel.removeAll();
					for (CardInstance card : army.getCardsById().values()) {
						armyPanel.add(new ArmyCardPanel(card));

					}
					armyPanel.validate();
					armyPanel.repaint();
				}
				SwingUtilities.getWindowAncestor(GameView.this).pack();
			}
		});
	}

	@Override
	public void refresh() {
		
	}

}
