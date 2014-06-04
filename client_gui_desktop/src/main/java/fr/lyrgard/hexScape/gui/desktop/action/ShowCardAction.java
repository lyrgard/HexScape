package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
				JLabel lbl = new JLabel(image);
			    JOptionPane.showMessageDialog(null, lbl, "Card", 
			                                 JOptionPane.PLAIN_MESSAGE, null);
				
			}
		});
	}
	
	
}
