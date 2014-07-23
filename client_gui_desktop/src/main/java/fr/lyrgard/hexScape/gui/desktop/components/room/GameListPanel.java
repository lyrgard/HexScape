package fr.lyrgard.hexScape.gui.desktop.components.room;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.OpenNewGameDialogAction;
import fr.lyrgard.hexScape.gui.desktop.components.HexaFont;
import fr.lyrgard.hexScape.gui.desktop.message.GameSelectedMessage;
import fr.lyrgard.hexScape.gui.desktop.view.room.GameListModel;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.room.Room;

public class GameListPanel extends JPanel{

	private static final long serialVersionUID = 1506378477490002770L;
	
	private GameListModel gameListModel;
	
	public GameListPanel() {
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("GAMES");
		title.setFont(HexaFont.getFont().deriveFont(35f));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title, BorderLayout.PAGE_START);
		
		gameListModel = new GameListModel();
		final JList<Game> gameList = new JList<Game>(gameListModel); 
		gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gameList.setLayoutOrientation(JList.VERTICAL);
		gameList.setVisibleRowCount(0);
		gameList.setCellRenderer(new GameCellRenderer());
		JScrollPane gameListScroller = new JScrollPane(gameList);
		gameListScroller.setPreferredSize(new Dimension(200, 400));
		add(gameListScroller, BorderLayout.CENTER);
		
		gameList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if (!e.getValueIsAdjusting()) {
					Game game = gameList.getSelectedValue();
					GuiMessageBus.post(new GameSelectedMessage(game));
				}
			}
		});
		
		JPanel buttonsPanel = new JPanel();
		add(buttonsPanel, BorderLayout.PAGE_END);
		
		buttonsPanel.add(new JButton(new OpenNewGameDialogAction(true, getTopLevelAncestor())));
		
		GuiMessageBus.register(this);
		
	}
	
	@Subscribe public void onRoomJoined(RoomJoinedMessage message) {
		final Room room = message.getRoom();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				gameListModel.removeAllGames();
				for (Game game : room.getGames()) {
					gameListModel.addGame(game);
				}
			}
		});
	}
	
	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		final Game game = message.getGame();

		if (game != null) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					gameListModel.addGame(game);
				}
			});
		}
	}
	
	@Subscribe public void onGameEnded(GameEndedMessage message) {
		final String gameId = message.getGameId();

		if (gameId != null) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					gameListModel.removeGame(gameId);
				}
			});
		}
	}
	
	@Subscribe public void onGameJoined(GameJoinedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				gameListModel.redraw();
			}
		});
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				gameListModel.redraw();
			}
		});
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				gameListModel.redraw();
			}
		});
	}
}
