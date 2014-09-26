package fr.lyrgard.hexScape.gui.desktop.view.room.users;


import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;

public class UserList extends JScrollPane {

	private static final long serialVersionUID = 8818305200720890666L;

	public UserList (UserListModel userListModel) {
		JList<User> userList = new JList<User>(userListModel); 
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(0);
		userList.setCellRenderer(new UserCellRenderer());
		setViewportView(userList);
	}
}
