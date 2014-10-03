package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.AddPieceAction;
import fr.lyrgard.hexScape.gui.desktop.message.CardSelectedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.MarkerService;

public class ArmyCardPanel extends JPanel {

	private static final long serialVersionUID = 6580633451061376631L;

	private CardInstance card;

	private ImageIcon imageIcon;

	private int piecesNumber = 0;

	private JLabel figureNumbersLabel;
	private JButton addFigureButton;

	private JPanel markerPanel;

	public ArmyCardPanel(final CardInstance card, String playerId) {
		this.card = card;

		setLayout(new BorderLayout());
		markerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
		add(markerPanel, BorderLayout.CENTER);
		
		CardType cardType = CardService.getInstance().getCardInventory().getCardsById().get(card.getCardTypeId());
		
		imageIcon = new ImageIcon(new File(cardType.getIconPath()).getAbsolutePath());

		piecesNumber = card.getNumber() * cardType.getFigureNames().size();
		card.getPieceLeftToPlace().clear();
		
		List<String> pieceLeft = new ArrayList<>(); 
		for (int i = 0; i < card.getNumber(); i++ ) {
			for (String figureName : cardType.getFigureNames()) {
				pieceLeft.add(figureName);
			}
		}
		// Remove already placed pieces
		for (PieceInstance piece :card.getPieces()) {
			pieceLeft.remove(piece.getModelId());
		}
		card.getPieceLeftToPlace().addAll(pieceLeft);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		add(buttonPanel, BorderLayout.PAGE_END);
		
		figureNumbersLabel = new JLabel();
		addFigureButton = new JButton();
		
		selectNextPiece();
		addFigureButton.setAlignmentX(24);
		buttonPanel.add(addFigureButton);
		buttonPanel.add(figureNumbersLabel);
		if (card.getPieceLeftToPlace().size() == 0) {
			addFigureButton.setEnabled(false);
		}

		JLabel imageLabel = new JLabel(imageIcon);
		add(imageLabel, BorderLayout.LINE_END);

		if (CurrentUserInfo.getInstance().getPlayerId().equals(playerId)) {
			imageLabel.addMouseListener(new PopMenuClickListener(new ArmyCardMenu(card)));
		}
		imageLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					GuiMessageBus.post(new CardSelectedMessage(card));
				}
			}
			
		});

		Border border = BorderFactory.createLineBorder(Color.BLACK, 2, true);
		
		border = BorderFactory.createTitledBorder(border, cardType.getName(), TitledBorder.LEFT, TitledBorder.TOP);
		setBorder(border);

		
		setSize(180, 125);
		setMaximumSize(new Dimension(175, 125));
		setPreferredSize(new Dimension(175, 125));
		
		reDrawMarkers();

		GuiMessageBus.register(this);
	}


	private void selectNextPiece() {
		figureNumbersLabel.setText(card.getPieceLeftToPlace().size() + "/" + piecesNumber);
		addFigureButton.setAction(new AddPieceAction(card.getPieceLeftToPlace().peek(), card));
		addFigureButton.setText("");
	}


	@Subscribe public void onPiecePlaced(final PiecePlacedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String playerId = message.getPlayerId();
				String cardId = message.getCardInstanceId();
				
				Game game = CurrentUserInfo.getInstance().getGame();
				
				if (game != null) {

					if (card.getId().equals(cardId)) {
						card.getPieceLeftToPlace().poll();
						selectNextPiece();
						if (card.getPieceLeftToPlace().size() == 0) {
							addFigureButton.setEnabled(false);
						} else if (StringUtils.equals(playerId, CurrentUserInfo.getInstance().getPlayerId())) {
							addFigureButton.getAction().actionPerformed(null);
						}
					}
				}
			}
		});
	}

	@Subscribe public void onPieceRemoved(final PieceRemovedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String pieceId = message.getPieceId();

				PieceInstance piece = card.getPiece(pieceId);
				if (piece != null) {
					if (piece != null && piece.getCard() != null) {
						String cardId = piece.getCard().getId();
						if (card.getId().equals(cardId)) {
							card.getPieceLeftToPlace().add(piece.getModelId());
							if (card.getPieceLeftToPlace().size() == 1) {
								addFigureButton.setEnabled(true);
							}
							selectNextPiece();
						}

					}

				}
			}
		});
	}
	
	private void reDrawMarkers() {
		markerPanel.removeAll();
		for (MarkerInstance marker : card.getMarkers()) {
			ImageIcon markerIcon = null;
			final JLabel imageLabel = new JLabel();
			MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());
			switch (markerDefinition.getType()) {
			case NORMAL:
				markerIcon = new ImageIcon(markerDefinition.getImage().getAbsolutePath());
				break;
			case STACKABLE:
				markerIcon = new ImageIcon(markerDefinition.getImage().getAbsolutePath());
				int number = ((StackableMarkerInstance)marker).getNumber();
				String numberString = Integer.toString(number);
				imageLabel.setText(numberString);
				imageLabel.setIconTextGap(1);
				break;
			case REVEALABLE:
				markerIcon = new ImageIcon(markerDefinition.getImage().getAbsolutePath());
				break;
			case HIDDEN:
				markerIcon = new ImageIcon(markerDefinition.getImage().getAbsolutePath());

				String hiddenMarkerTypeId = ((HiddenMarkerInstance)marker).getHiddenMarkerDefinitionId();
				if (StringUtils.isNotEmpty(hiddenMarkerTypeId)) {
					MarkerDefinition hiddenMarkerDefinition = MarkerService.getInstance().getMarkersByIds().get(hiddenMarkerTypeId);
					final ImageIcon hiddenMarkerIcon = new ImageIcon(hiddenMarkerDefinition.getImage().getAbsolutePath());
					final ImageIcon normalMarkerIcon = markerIcon;
					
					imageLabel.addMouseListener(new PopMenuClickListener(new RevealableMarkerMenu(card, (HiddenMarkerInstance)marker)));
					imageLabel.addMouseListener(new MouseAdapter() {
						@Override
			            public void mouseEntered(java.awt.event.MouseEvent evt) {
							imageLabel.setIcon(hiddenMarkerIcon);
			            }

			            @Override
			            public void mouseExited(java.awt.event.MouseEvent evt) {
			            	imageLabel.setIcon(normalMarkerIcon);
			            }
					});
				}
				break;
			}
			imageLabel.setIcon(markerIcon);
			markerPanel.add(imageLabel);
		}

		markerPanel.validate();
		markerPanel.repaint();
	}

	@Subscribe public void onMarkerPlaced(final MarkerPlacedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String cardId = message.getCardId();
				if (ArmyCardPanel.this.card.getId().equals(cardId)) {
					reDrawMarkers();
				}
			}
		});
	}
	
	@Subscribe public void onMarkerRemoved(final MarkerRemovedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String cardId = message.getCardId();
				if (ArmyCardPanel.this.card.getId().equals(cardId)) {
					reDrawMarkers();
				}
			}
		});
	}
	
	@Subscribe public void onMarkerRevealed(final MarkerRevealedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String cardId = message.getCardId();
				if (ArmyCardPanel.this.card.getId().equals(cardId)) {
					reDrawMarkers();
				}
			}
		});
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		String playerId = message.getPlayerId();
		
		if (StringUtils.equals(playerId, CurrentUserInfo.getInstance().getPlayerId())) {
			GuiMessageBus.unregister(this);
		}
	}
}
