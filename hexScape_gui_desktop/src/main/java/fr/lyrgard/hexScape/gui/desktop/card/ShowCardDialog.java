package fr.lyrgard.hexScape.gui.desktop.card;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import fr.lyrgard.hexScape.model.Card;

public class ShowCardDialog extends JDialog {

	private static final long serialVersionUID = 4628708458934864119L;

	private Image image;
	
	private int width;
	private int height;
	
	public ShowCardDialog(Card card) {
		try {
			image = ImageIO.read(new File(card.getFolder(), "card.jpg"));
			
			//ImageI
			
			height = image.getHeight(new ImageObserver() {
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					ShowCardDialog.this.setPreferredSize(new Dimension(width, height));
					return true;
				}
			});
			width = image.getWidth(null);
			
			if (height != -1) {
				setPreferredSize(new Dimension(width, height));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
