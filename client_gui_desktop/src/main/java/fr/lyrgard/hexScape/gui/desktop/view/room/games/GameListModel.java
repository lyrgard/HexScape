package fr.lyrgard.hexScape.gui.desktop.view.room.games;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;

public class GameListModel extends AbstractListModel<Game> {
	
	private static final long serialVersionUID = 8030825700166419693L;

	private static final GameNameComparator comparator = new GameNameComparator();

	private List<Game> games = new ArrayList<Game>();
	
	public int getSize() {
		return games.size();
	}

	public Game getElementAt(int index) {
		return games.get(index);
	}
	
	private void setGames(Collection<Game> games) {
		this.games = new ArrayList<Game>(games);
		Collections.sort(this.games, comparator);
		fireContentsChanged(this, 0, games.size());
	}
	
	public void redraw() {
		setGames(Universe.getInstance().getGamesByGameIds().values());
	}

	private static class GameNameComparator implements Comparator<Game> {

		public int compare(Game g1, Game g2) {
			if (g1.isStarted()) {
				if (g2.isStarted()) {
					return g1.getName().compareTo(g2.getName());
				} else {
					return 1;
				}
			} else {
				if (g2.isStarted()) {
					return -1;
				} else {
					return g1.getName().compareTo(g2.getName());
				}
			}
		}
	}
}
