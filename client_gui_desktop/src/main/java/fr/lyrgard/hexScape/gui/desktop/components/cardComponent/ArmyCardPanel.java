package fr.lyrgard.hexScape.gui.desktop.components.cardComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.AddPieceAction;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.ArmyCardMenu;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.PopMenuClickListener;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.RevealableMarkerMenu;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class ArmyCardPanel extends JPanel {

	private static final long serialVersionUID = 6580633451061376631L;

	private CardInstance card;

	private ImageIcon imageIcon;

	private int piecesNumber = 0;

	private JLabel figureNumbersLabel;
	private JButton addFigureButton;
	

	private Queue<String> pieceLeftToPlace = new LinkedList<String>();

	private JPanel markerPanel;

	public ArmyCardPanel(CardInstance card) {
		this.card = card;

		setLayout(new BorderLayout());
		markerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
		add(markerPanel, BorderLayout.CENTER);

		this.setSize(200, 50);
		imageIcon = new ImageIcon(new File(card.getType().getFolder(), "icon.jpg").getAbsolutePath());

		piecesNumber = card.getNumber() * card.getType().getFigureNames().size();
		for (int i = 0; i < card.getNumber(); i++ ) {
			for (String figureName : card.getType().getFigureNames()) {
				pieceLeftToPlace.add(figureName);
			}
		}
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		add(buttonPanel, BorderLayout.PAGE_END);
		
		figureNumbersLabel = new JLabel();
		addFigureButton = new JButton();
		
		selectNextPiece();
		addFigureButton.setAlignmentX(24);
		buttonPanel.add(addFigureButton);
		buttonPanel.add(figureNumbersLabel);

		JLabel imageLabel = new JLabel(imageIcon);
		add(imageLabel, BorderLayout.LINE_END);

		imageLabel.addMouseListener(new PopMenuClickListener(new ArmyCardMenu(card)));

		Border border = BorderFactory.createLineBorder(Color.BLACK, 2, true);
		border = BorderFactory.createTitledBorder(border, card.getType().getName(), TitledBorder.LEFT, TitledBorder.TOP);
		setBorder(border);

		setPreferredSize(new Dimension(150, 120));
		setMaximumSize(new Dimension(150, 120));

		MessageBus.register(this);
	}


	private void selectNextPiece() {
		figureNumbersLabel.setText(pieceLeftToPlace.size() + "/" + piecesNumber);
		addFigureButton.setAction(new AddPieceAction(pieceLeftToPlace.peek(), card));
	}

	@Subscribe public void onPiecePlaced(final PiecePlacedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String playerId = message.getPlayerId();
				String cardId = message.getCardInstanceId();
				
				Player player = Universe.getInstance().getPlayersByIds().get(playerId);
				if (player != null) {

					if (card.getId().equals(cardId)) {
						pieceLeftToPlace.poll();
						selectNextPiece();
						if (pieceLeftToPlace.size() == 0) {
							addFigureButton.setEnabled(false);
						} 

					}
				}
			}
		});
	}

	@Subscribe public void onPieceRemoved(final PieceRemovedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String playerId = message.getPlayerId();
				String pieceId = message.getPieceId();
				String cardId = message.getCardInstanceId();
				
				Player player = Universe.getInstance().getPlayersByIds().get(playerId);
				if (player != null) {
					PieceInstance piece = player.getPiecesById().get(pieceId);
					if (piece != null) {
						if (card.getId().equals(cardId)) {
							pieceLeftToPlace.add(piece.getModelId());
							if (pieceLeftToPlace.size() == 1) {
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
			JLabel imageLabel = new JLabel();
			switch (marker.getMarkerDefinition().getType()) {
			case NORMAL:
				markerIcon = new ImageIcon(marker.getMarkerDefinition().getImage().getAbsolutePath());
				break;
			case STACKABLE:
				markerIcon = new ImageIcon(marker.getMarkerDefinition().getImage().getAbsolutePath());
				int number = ((StackableMarkerInstance)marker).getNumber();
				String numberString = Integer.toString(number);
				imageLabel.setText(numberString);
				imageLabel.setIconTextGap(1);
				break;
			case REVEALABLE:
				boolean hidden = ((RevealableMarkerInstance)marker).isHidden();
				if (hidden) {
					markerIcon = new ImageIcon(((RevealableMarkerDefinition)marker.getMarkerDefinition()).getOwnerHiddenMarkerImage().getAbsolutePath());
					imageLabel.addMouseListener(new PopMenuClickListener(new RevealableMarkerMenu(card, (RevealableMarkerInstance)marker)));
				} else {
					markerIcon = new ImageIcon(marker.getMarkerDefinition().getImage().getAbsolutePath());
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
}
