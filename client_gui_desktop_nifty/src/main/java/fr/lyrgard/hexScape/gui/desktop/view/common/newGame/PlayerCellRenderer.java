package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fr.lyrgard.hexScape.model.player.Player;

public class PlayerCellRenderer extends JLabel implements ListCellRenderer<Player> {

	private static final long serialVersionUID = -1609619038664081787L;
	
	public PlayerCellRenderer() {
		setOpaque(true);
//		setText(text);
//		setLayout(new BorderLayout());
//		statusIcon = new JLabel();
//		playerName = new JLabel();
//		playerName.setFont(playerName.getFont().deriveFont(15f));
//		
//		add(statusIcon, BorderLayout.LINE_START);
//		add(playerName, BorderLayout.CENTER);
//		//Border border = BorderFactory.createLineBorder(Color.black);
//		statusIcon.setBorder(new EmptyBorder(0, 0, 0, 10));
//		Border padding = new EmptyBorder(10, 10, 10, 10);
//		setBorder(padding);
	}
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Player> list, Player player, int index, boolean isSelected, boolean cellHasFocus) {
		String text = player.getDisplayName();
		
		setText(text);

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
}
