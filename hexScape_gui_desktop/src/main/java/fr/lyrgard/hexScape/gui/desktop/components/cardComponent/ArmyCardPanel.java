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

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.marker.MarkerRevealedOnCardEvent;
import fr.lyrgard.hexScape.event.marker.MarkersOnCardChangedEvent;
import fr.lyrgard.hexScape.event.piece.PieceAddedEvent;
import fr.lyrgard.hexScape.event.piece.PieceRemovedEvent;
import fr.lyrgard.hexScape.gui.desktop.action.AddPieceAction;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.ArmyCardMenu;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.PopMenuClickListener;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.RevealableMarkerMenu;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;

public class ArmyCardPanel extends JPanel {

	private static final long serialVersionUID = 6580633451061376631L;

	private Card card;

	private ImageIcon imageIcon;

	private int piecesNumber = 0;

	private JLabel figureNumbersLabel;
	private JButton addFigureButton;
	

	private Queue<String> pieceLeftToPlace = new LinkedList<String>();

	private JPanel markerPanel;

	public ArmyCardPanel(Card card, int number) {
		this.card = card;

		setLayout(new BorderLayout());
		markerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
		add(markerPanel, BorderLayout.CENTER);

		this.setSize(200, 50);
		imageIcon = new ImageIcon(new File(card.getFolder(), "icon.jpg").getAbsolutePath());

		piecesNumber = number * card.getFigureNames().size();
		for (int i = 0; i < number; i++ ) {
			for (String figureName : card.getFigureNames()) {
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
		border = BorderFactory.createTitledBorder(border, card.getName(), TitledBorder.LEFT, TitledBorder.TOP);
		setBorder(border);

		setPreferredSize(new Dimension(150, 120));
		setMaximumSize(new Dimension(150, 120));

		HexScapeCore.getInstance().getEventBus().register(this);
	}


	private void selectNextPiece() {
		figureNumbersLabel.setText(piecesNumber + "/" + pieceLeftToPlace.size());
		addFigureButton.setAction(new AddPieceAction(pieceLeftToPlace.peek()));
	}

	@Subscribe public void pieceAdded(final PieceAddedEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				MoveablePiece piece = event.getPiece();
				if (card.getFigureNames().contains(piece.getModelName())) {
					pieceLeftToPlace.poll();
					selectNextPiece();
					if (pieceLeftToPlace.size() == 0) {
						addFigureButton.setEnabled(false);
					} 

				}
			}
		});
	}

	@Subscribe public void pieceRemoved(final PieceRemovedEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				MoveablePiece piece = event.getPiece();
				if (card.getFigureNames().contains(piece.getModelName())) {
					pieceLeftToPlace.add(piece.getModelName());
					if (pieceLeftToPlace.size() == 1) {
						addFigureButton.setEnabled(true);
					}
					selectNextPiece();
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

	@Subscribe public void markerAdded(final MarkersOnCardChangedEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Card card = event.getCard();
				if (ArmyCardPanel.this.card == card) {
					reDrawMarkers();
				}
			}
		});
	}
	
	@Subscribe public void markerRevealed(final MarkerRevealedOnCardEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Card card = event.getCard();
				if (ArmyCardPanel.this.card == card) {
					reDrawMarkers();
				}
			}
		});
	}
}
