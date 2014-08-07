package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import fr.lyrgard.hexScape.model.card.CardType;

public class ShowCardAction extends AbstractAction {
	
	private static final long serialVersionUID = 54515381264329889L;
	
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/zoom.png"));
	
	private CardType card;

	public ShowCardAction(CardType card) {
		super("", icon);
		this.card = card;
	}

	public void actionPerformed(ActionEvent e) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				ImageIcon image = new ImageIcon(new File(card.getFolder(), "card.jpg").getAbsolutePath());
				int height = image.getIconHeight();
				int width = image.getIconWidth();
				Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
				if (height > (screenDimension.height - 130)) {
					height = screenDimension.height - 130;
				}
				if (width > screenDimension.width - 10) {
					width = screenDimension.width - 10;
				}
				
				JLabel lbl = new JLabel(image);
				JScrollPane scrollPane = new JScrollPane(lbl);
				scrollPane.setPreferredSize( new Dimension( width + 22, height + 22 ) );
			    JOptionPane.showMessageDialog(null, scrollPane, "Card", 
			                                 JOptionPane.PLAIN_MESSAGE, null);
				
			}
		});
	}
	
	
}
