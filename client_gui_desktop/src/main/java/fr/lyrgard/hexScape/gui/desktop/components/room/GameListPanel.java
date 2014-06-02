package fr.lyrgard.hexScape.gui.desktop.components.room;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.room.RoomContentReceivedEvent;
import fr.lyrgard.hexScape.gui.desktop.action.StartNewGameAction;
import fr.lyrgard.hexScape.gui.desktop.view.room.GameListModel;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.room.Room;

public class GameListPanel extends JPanel{

	private static final long serialVersionUID = 1506378477490002770L;
	
	private GameListModel gameListModel;
	
	public GameListPanel() {
		setLayout(new BorderLayout());
		
		gameListModel = new GameListModel();
		JList<Game> gameList = new JList<Game>(gameListModel); 
		gameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		gameList.setLayoutOrientation(JList.VERTICAL);
		gameList.setVisibleRowCount(0);
		JScrollPane gameListScroller = new JScrollPane(gameList);
		gameListScroller.setPreferredSize(new Dimension(200, 400));
		add(gameListScroller, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		add(buttonsPanel, BorderLayout.PAGE_END);
		
		buttonsPanel.add(new JButton(new StartNewGameAction(true)));
		
		HexScapeCore.getInstance().getEventBus().register(this);
	}
	
	@Subscribe public void onRoomContentReceived(RoomContentReceivedEvent event) {
		Room room = event.getRoom();
		gameListModel.setGames(room.getGames());
		
	}
}
