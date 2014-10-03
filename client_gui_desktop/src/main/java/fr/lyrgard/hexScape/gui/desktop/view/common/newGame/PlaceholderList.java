package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import fr.lyrgard.hexScape.model.player.Player;

public class PlaceholderList extends JScrollPane {
	
	private static final long serialVersionUID = 5451973608434855593L;
	
	private JList<Player> placeholderList;

	public PlaceholderList (PlayerListModel placeholderListModel) {
		placeholderList = new JList<Player>(placeholderListModel); 
		placeholderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		placeholderList.setLayoutOrientation(JList.VERTICAL);
		placeholderList.setVisibleRowCount(0);
		placeholderList.setCellRenderer(new PlayerCellRenderer());
		setViewportView(placeholderList);
	}
	
	public void addListSelectionListener(ListSelectionListener listener) {
		placeholderList.addListSelectionListener(listener);
	}
	
	public Player getSelectedValue() {
		return placeholderList.getSelectedValue();
	}
}
