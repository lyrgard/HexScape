package fr.lyrgard.hexScape.gui.desktop.components.cardComponent;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import fr.lyrgard.hexScape.event.piece.PieceSelectedEvent;
import fr.lyrgard.hexScape.event.piece.PieceUnselectedEvent;
import fr.lyrgard.hexScape.gui.desktop.action.ShowCardAction;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.MoveablePiece;

public class SelectedCardPanel extends JPanel {

	private static final long serialVersionUID = 2391039578032412686L;
	
	private Image image;
	
	JPanel buttonPanel;
	
	public SelectedCardPanel() {
		
		
		setPreferredSize(new Dimension(150, 200));
		setMaximumSize(new Dimension(150, 200));
		
		setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		buttonPanel.setOpaque(false);
		this.add(buttonPanel,BorderLayout.PAGE_END);
		
		HexScapeCore.getInstance().getEventBus().register(this);
	}
	
	@Subscribe public void onPieceSelected(PieceSelectedEvent event) {
		MoveablePiece piece = event.getPiece();
		Card card = HexScapeCore.getInstance().getCardService().getCardByPieceId(piece.getModelName());
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
	
	@Subscribe public void onPieceUnselected(PieceUnselectedEvent event) {
		buttonPanel.removeAll();
		image = null;
		validate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}
	
	
}
