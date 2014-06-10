package fr.lyrgard.hexScape.gui.desktop.view.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

import fr.lyrgard.hexScape.model.player.Player;

public class PlayerListModel extends AbstractListModel<Player> {
	
	private static final long serialVersionUID = -2989657265872205507L;

	private static final PlayerNameComparator comparator = new PlayerNameComparator();

	private List<Player> players = new ArrayList<Player>();
	
	public int getSize() {
		return players.size();
	}

	public Player getElementAt(int index) {
		return players.get(index);
	}
	
	public void setPlayers(Collection<Player> players) {
		this.players = new ArrayList<Player>(players);
		Collections.sort(this.players, comparator);
		fireContentsChanged(this, 0, players.size());
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
		setPlayers(players);
	}

	public void addPlayer(Player player) {
		players.add(player);
		setPlayers(players);
	}
	
	private static class PlayerNameComparator implements Comparator<Player> {

		public int compare(Player p1, Player p2) {
			if (p1 == null) {
				if (p2 == null) {
					return 0;
				} else {
					return -1;
				}
			} else {
				if (p2 == null) {
					return 1;
				} else {
					return p1.getName().compareTo(p2.getName());
				}
			}
			
		}
		
	}



	
}
