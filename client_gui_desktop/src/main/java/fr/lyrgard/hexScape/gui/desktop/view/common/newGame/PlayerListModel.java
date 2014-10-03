package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

import fr.lyrgard.hexScape.model.player.Player;

public class PlayerListModel extends AbstractListModel<Player> {

	private static final long serialVersionUID = -4581991373186072190L;
	
	private static final PlayerNameComparator comparator = new PlayerNameComparator();
	
	private List<Player> players;

	public PlayerListModel() {
		this.players = new ArrayList<Player>();
	}
	
	public PlayerListModel(Collection<Player> placeholders) {
		this.players = new ArrayList<Player>(placeholders);
	}
	
	@Override
	public int getSize() {
		return players.size();
	}

	@Override
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

	public void removeAllPlayers() {
		players.clear();
		setPlayers(players);
	}

	
	public void addPlayer(Player player) {
		players.add(player);
		setPlayers(players);
	}
	
	public void redraw() {
		fireContentsChanged(this, 0, players.size());
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
				boolean p1Free = p1.getUserId() == null;
				boolean p2Free = p2.getUserId() == null;
				
				if (p1Free == p2Free) {
					return p1.getName().compareTo(p2.getName());
				} else {
					if (p1Free) {
						return 1;
					} else {
						return -1;
					}
				}
			}
		}
	}

}
