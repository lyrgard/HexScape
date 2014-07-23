package fr.lyrgard.hexScape.gui.desktop.components.player;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import fr.lyrgard.hexScape.gui.desktop.components.room.GameCellRenderer;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class PlayerCellRenderer extends JPanel implements ListCellRenderer<Player> {

	private static final long serialVersionUID = -1609619038664081787L;
	
	private static final ImageIcon redFlagIcon = new ImageIcon(GameCellRenderer.class.getResource("/gui/icons/flag_red.png"));
	private static final ImageIcon greenFlagIcon = new ImageIcon(GameCellRenderer.class.getResource("/gui/icons/flag_green.png"));
	private static final ImageIcon orangeFlagIcon = new ImageIcon(GameCellRenderer.class.getResource("/gui/icons/flag_orange.png"));
	
	private JLabel statusIcon;
	private JLabel playerName;
	
	public PlayerCellRenderer() {
		setLayout(new BorderLayout());
		statusIcon = new JLabel();
		playerName = new JLabel();
		playerName.setFont(playerName.getFont().deriveFont(15f));
		
		add(statusIcon, BorderLayout.LINE_START);
		add(playerName, BorderLayout.CENTER);
		//Border border = BorderFactory.createLineBorder(Color.black);
		statusIcon.setBorder(new EmptyBorder(0, 0, 0, 10));
		Border padding = new EmptyBorder(10, 10, 10, 10);
		setBorder(padding);
	}
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Player> list, Player player, int index, boolean isSelected, boolean cellHasFocus) {
		playerName.setText(player.getName());
		if (player.getGameId() != null) {
			Game game = Universe.getInstance().getGamesByGameIds().get(player.getGameId());
			
			if (game != null) {
				if (game.isStarted()) {
					statusIcon.setIcon(redFlagIcon);
				} else {
					statusIcon.setIcon(orangeFlagIcon);
				}
			}
		} else {
			statusIcon.setIcon(greenFlagIcon);
		}
		
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
