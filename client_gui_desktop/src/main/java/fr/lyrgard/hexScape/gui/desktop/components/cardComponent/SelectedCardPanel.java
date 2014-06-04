package fr.lyrgard.hexScape.gui.desktop.components.cardComponent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.ShowCardAction;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.player.Player;

public class SelectedCardPanel extends JPanel {

	private static final long serialVersionUID = 2391039578032412686L;
	
	private Image image;
	
	private JPanel buttonPanel;
	
	public SelectedCardPanel() {
		
		
		setPreferredSize(new Dimension(150, 200));
		setMaximumSize(new Dimension(150, 200));
		
		setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		buttonPanel.setOpaque(false);
		this.add(buttonPanel,BorderLayout.PAGE_END);
		
		MessageBus.register(this);
	}
	
	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {
		final String playerId = message.getPlayerId();
		final String cardId = message.getCardInstanceId();
		
		if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					Player player = Universe.getInstance().getPlayersByIds().get(playerId);
					if (player != null && player.getArmy() != null) {
						CardInstance cardInstance = player.getArmy().getCardsById().get(cardId);
						if (cardInstance != null) {
							CardType card = cardInstance.getType();
							buttonPanel.removeAll();
							image = null;
							if (card != null) {
								try {
									image = ImageIO.read(new File(card.getFolder(), "card.jpg"));
									image = image.getScaledInstance(-1, 200, Image.SCALE_SMOOTH);
								} catch (IOException e) {
									e.printStackTrace();
								}
								buttonPanel.add(new JButton(new ShowCardAction(card)));

							}
							validate();
							repaint();
						}
					}
				}
			});
		}
	}
	
	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		final String playerId = message.getPlayerId();
		
		if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					buttonPanel.removeAll();
					image = null;
					validate();
					repaint();
				}
			});
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}
	
	
}
