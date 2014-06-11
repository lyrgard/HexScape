package fr.lyrgard.hexScape.gui.desktop.view.room;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class JoinedGamePanel extends JPanel {

	private static final long serialVersionUID = 1356336562513756827L;
	
	private PlayerListModel playerListModel;
	
	private JLabel gameTitle = new JLabel();
	
	public JoinedGamePanel(Game game) {
		
		gameTitle.setText(game.getName());
		add(gameTitle, BorderLayout.PAGE_START);
		
		
		
		playerListModel = new PlayerListModel();
		
		for (String playerId : game.getPlayersIds()) {
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);
			if (player != null) {
				playerListModel.addPlayer(player);
			}
		}
		
		JList<Player> userList = new JList<Player>(playerListModel); 
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(0);
		JScrollPane userListScroller = new JScrollPane(userList);
		userListScroller.setPreferredSize(new Dimension(200, 400));
		add(userListScroller, BorderLayout.LINE_END);
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.LINE_START);
		
		JButton startButton = new JButton("Start");
		buttonPanel.add(startButton);
		
		Canvas panel3d = HexScapeFrame.getInstance().getPanel3d();
		add(panel3d, BorderLayout.CENTER);
	}
}
