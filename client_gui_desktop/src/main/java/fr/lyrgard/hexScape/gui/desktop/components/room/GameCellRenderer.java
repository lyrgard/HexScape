package fr.lyrgard.hexScape.gui.desktop.components.room;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import fr.lyrgard.hexScape.model.game.Game;

public class GameCellRenderer extends JPanel implements ListCellRenderer<Game> {

	private static final long serialVersionUID = -1609619038664081787L;
	
	private static final ImageIcon redFlagIcon = new ImageIcon(GameCellRenderer.class.getResource("/gui/icons/flag_red.png"));
	private static final ImageIcon greenFlagIcon = new ImageIcon(GameCellRenderer.class.getResource("/gui/icons/flag_green.png"));
	private static final ImageIcon orangeFlagIcon = new ImageIcon(GameCellRenderer.class.getResource("/gui/icons/flag_orange.png"));
	
	private JLabel statusIcon;
	private JLabel gameName;
	private JLabel info;
	
	public GameCellRenderer() {
		setLayout(new BorderLayout());
		statusIcon = new JLabel();
		gameName = new JLabel();
		gameName.setFont(gameName.getFont().deriveFont(12f));
		gameName.setHorizontalAlignment(SwingConstants.CENTER);
		info = new JLabel();
		info.setFont(gameName.getFont().deriveFont(15f));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(gameName, BorderLayout.PAGE_START);
		add(statusIcon, BorderLayout.LINE_START);
		add(info, BorderLayout.CENTER);
		Border border = BorderFactory.createLineBorder(Color.black);
		Border padding = new EmptyBorder(10, 10, 10, 10);
		setBorder(new CompoundBorder(border, padding));
	}
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Game> list, Game game, int index, boolean isSelected, boolean cellHasFocus) {
		gameName.setText(game.getName());
		if (game.isStarted()) {
			statusIcon.setIcon(redFlagIcon);
		} else {
			if (game.getPlayerNumber() > game.getPlayersIds().size()) {
				statusIcon.setIcon(greenFlagIcon);
			} else {
				statusIcon.setIcon(orangeFlagIcon);
			}
		}
		info.setText(game.getPlayersIds().size() + "/" + game.getPlayerNumber());
		
		
		
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
