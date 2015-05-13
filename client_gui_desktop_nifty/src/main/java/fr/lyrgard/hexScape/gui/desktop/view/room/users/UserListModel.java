package fr.lyrgard.hexScape.gui.desktop.view.room.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

import fr.lyrgard.hexScape.model.player.User;

public class UserListModel extends AbstractListModel<User> {
	
	private static final long serialVersionUID = -2989657265872205507L;

	private static final UserNameComparator comparator = new UserNameComparator();

	private List<User> users = new ArrayList<User>();
	
	public int getSize() {
		return users.size();
	}

	public User getElementAt(int index) {
		return users.get(index);
	}
	
	public void setUsers(Collection<User> users) {
		int oldSize = users.size();
		this.users = new ArrayList<User>(users);
		Collections.sort(this.users, comparator);
		fireContentsChanged(this, 0, Math.max(oldSize, users.size()));
	}
	
	public void removeUser(User user) {
		users.remove(user);
		setUsers(users);
	}

	public void removeAllUsers() {
		users.clear();
		setUsers(users);
	}

	
	public void addUser(User user) {
		users.add(user);
		setUsers(users);
	}
	
	public void redraw() {
		fireContentsChanged(this, 0, users.size());
	}
	
	private static class UserNameComparator implements Comparator<User> {

		public int compare(User p1, User p2) {
			
			if (p1 == null) {
				if (p2 == null) {
					return 0;
				} else {
					return -1;
				}
			} else {
//				Integer p1GameStatus = getGameStatus(p1);
//				Integer p2GameStatus = getGameStatus(p2);
				Integer p1GameStatus = 1;
				Integer p2GameStatus = 1;
				
				if (p1GameStatus.equals(p2GameStatus)) {
					return p1.getName().compareTo(p2.getName());
				} else {
					return p1GameStatus.compareTo(p2GameStatus);
				}
			}
		}
		
		// TODO
//		private int getGameStatus(User user) {
//			int status = 0; // 0 = no game, 1 = game joined but not started, 1 = game started
//			Player player = Universe.getInstance().getPlayersByUserIds().get(user.getId());
//			if (player != null && player.getGameId() != null) {
//				Game game = Universe.getInstance().getGamesByGameIds().get(player.getGameId());
//				if (game != null) {
//					if (game.isStarted()) {
//						status = 2;
//					} else {
//						status = 1;
//					}
//				}
//			}
//			return status;
//		}
	}



	
}
