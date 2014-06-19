package fr.lyrgard.hexScape.gui.desktop.components.room;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fr.lyrgard.hexScape.model.game.Game;

public class GameCellRenderer implements ListCellRenderer<Game> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Game> list, Game game, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = new JLabel(game.getName());
		label.setOpaque(true);
		
		if (isSelected) {
			label.setBackground(list.getSelectionBackground());
			label.setForeground(list.getSelectionForeground());
		} else {
			label.setBackground(list.getBackground());
			label.setForeground(list.getForeground());
		}
		return label;
	}

}
