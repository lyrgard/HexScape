package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

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

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.ShowCardAction;
import fr.lyrgard.hexScape.gui.desktop.message.CardSelectedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.service.CardService;

public class SelectedCardPanel extends JPanel {

	private static final long serialVersionUID = 2391039578032412686L;

	private Image image;

	private JPanel buttonPanel;

	public SelectedCardPanel() {

		setMinimumSize(new Dimension(UNDEFINED_CONDITION, 50));
		//setPreferredSize(new Dimension(200, 200));
		//setMaximumSize(new Dimension(-1, 200));

		setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		buttonPanel.setOpaque(false);
		this.add(buttonPanel,BorderLayout.PAGE_END);

		//setBorder(new LineBorder(Color.RED, 2));
		
		GuiMessageBus.register(this);
	}
	
	private void displayCard(CardType cardType) {
		buttonPanel.removeAll();
		image = null;
		if (cardType != null) {
			try {
				image = ImageIO.read(new File(cardType.getImagePath()));
				image = image.getScaledInstance(-1, 200, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}
			buttonPanel.add(new JButton(new ShowCardAction(cardType)));

		}
		validate();
		repaint();
	}
	
	@Subscribe public void onCardSelected(CardSelectedMessage message) {
		String cardTypeId = message.getCard().getCardTypeId();
		
		CardType card = CardService.getInstance().getCardInventory().getCardsById().get(cardTypeId);
		displayCard(card);
	}

	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceId = message.getPieceId();

		if (CurrentUserInfo.getInstance().getPlayerId().equals(playerId)) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());

					if (game != null) {
						PieceInstance piece = game.getPiece(pieceId);
						if (piece != null && piece.getCard() != null) {
							CardType card = CardService.getInstance().getCardInventory().getCardsById().get(piece.getCard().getCardTypeId());
							displayCard(card);
						}
					}
				}
			});
		}
	}


	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		final String playerId = message.getPlayerId();

		if (CurrentUserInfo.getInstance().getPlayerId().equals(playerId)) {
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

	@Subscribe public void onGameLeft(GameLeftMessage message) {
		if (CurrentUserInfo.getInstance().getPlayer() == null) {
			// current player is null == we left the game
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
