package fr.lyrgard.hexScape.gui.desktop.components.player;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fr.lyrgard.hexScape.model.player.Player;

public class PlayerCellRenderer implements ListCellRenderer<Player> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Player> list,	Player player, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = new JLabel(player.getName());
		
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
