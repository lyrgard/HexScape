package fr.lyrgard.hexScape.gui.desktop.components.player;


import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import fr.lyrgard.hexScape.gui.desktop.view.room.PlayerListModel;
import fr.lyrgard.hexScape.model.player.Player;

public class PlayerList extends JScrollPane {

	private static final long serialVersionUID = 8818305200720890666L;

	public PlayerList (PlayerListModel playerListModel) {
		JList<Player> userList = new JList<Player>(playerListModel); 
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(0);
		userList.setCellRenderer(new PlayerCellRenderer());
		setViewportView(userList);
	}
}
