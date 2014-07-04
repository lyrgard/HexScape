package fr.lyrgard.hexScape.gui.desktop.components.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.JoinGameAction;
import fr.lyrgard.hexScape.gui.desktop.action.StartGameAction;
import fr.lyrgard.hexScape.gui.desktop.message.GameSelectedMessage;
import fr.lyrgard.hexScape.gui.desktop.view.room.PlayerListModel;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class SelectedGamePanel extends JPanel {

	private static final long serialVersionUID = 1356336562513756827L;

	private PlayerListModel playerListModel;

	private JLabel gameTitle = new JLabel();

	private Game game;

	private JButton joinButton;

	private JButton startButton;

	public SelectedGamePanel() {

		setLayout(new BorderLayout());

		add(gameTitle, BorderLayout.PAGE_START);

		playerListModel = new PlayerListModel();
		JList<Player> userList = new JList<Player>(playerListModel); 
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(0);
		JScrollPane userListScroller = new JScrollPane(userList);
		userListScroller.setPreferredSize(new Dimension(100, 200));
		add(userListScroller, BorderLayout.LINE_END);

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.LINE_START);

		joinButton = new JButton();
		buttonPanel.add(joinButton);

		startButton = new JButton("Start");
		buttonPanel.add(startButton);

		GuiMessageBus.register(this);
	}

	public Game getGame() {
		return game;
	}

	@Subscribe public void gameSelected(GameSelectedMessage message) {

		game = message.getGame();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				View3d view3d = HexScapeFrame.getInstance().getView3d();
				view3d.setPreferredSize(new Dimension(UNDEFINED_CONDITION, 150));
				add(view3d, BorderLayout.CENTER);

				DisplayMapMessage displayMapMessage = new DisplayMapMessage(game.getId());
				CoreMessageBus.post(displayMapMessage);

				String title = getName();
				if (game.isStarted()) {
					title += " - ALREADY STARTED";
				} else if (game.getPlayerNumber() > game.getPlayersIds().size()) {
					title += " - WAITING PLAYERS";
				} else {
					title += " - READY TO START";
				}

				gameTitle.setText(title);

				playerListModel.removeAllPlayers();
				boolean canJoin = !game.isStarted() && (game.getPlayerNumber() > game.getPlayersIds().size());
				boolean canStart = false;
				for (String playerId : game.getPlayersIds()) {
					Player player = Universe.getInstance().getPlayersByIds().get(playerId);
					if (player != null) {
						playerListModel.addPlayer(player);
					}
					if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
						canJoin = false;
						canStart = !game.isStarted();
					}
				}
				if (canJoin) {
					Player player = Universe.getInstance().getPlayersByIds().get(HexScapeCore.getInstance().getPlayerId());
					if (player != null) {
						if (player.getGame() != null) {
							canJoin = false;
						}
					}
				}


				if (canJoin) {
					joinButton.setAction(new JoinGameAction(game.getId()));
				}
				joinButton.setVisible(canJoin);
				if (canStart) {
					startButton.setAction(new StartGameAction(game.getId()));
				}
				startButton.setVisible(canStart);

				setVisible(true);
			}
		});
	}

	@Subscribe public void onGameEnded(GameEndedMessage message) {
		final String gameId = message.getGameId();

		if (gameId != null) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					SelectedGamePanel.this.setVisible(false);
				}
			});
		}
	}

	@Subscribe public void gameJoined(GameJoinedMessage message) {

		final String gameId = message.getGameId();
		final String playerId = message.getPlayerId();

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				if (game.getId().equals(gameId)) {
					Player player = Universe.getInstance().getPlayersByIds().get(playerId);

					if (player != null) {
						playerListModel.addPlayer(player);

						if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
							joinButton.setVisible(false);
							startButton.setAction(new StartGameAction(gameId));
							startButton.setVisible(true);
							String title = getName();
							if (game.isStarted()) {
								title += " - ALREADY STARTED";
							} else if (game.getPlayerNumber() > game.getPlayersIds().size()) {
								title += " - WAITING PLAYERS";
							} else {
								title += " - READY TO START";
							}

							gameTitle.setText(title);
						}
					}
				}

			}
		});
	}

	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();

		if (gameId != null && gameId.equals(game.getId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					joinButton.setVisible(false);
					startButton.setVisible(false);
					String title = getName();
					if (game.isStarted()) {
						title += " - ALREADY STARTED";
					} else if (game.getPlayerNumber() > game.getPlayersIds().size()) {
						title += " - WAITING PLAYERS";
					} else {
						title += " - READY TO START";
					}

					gameTitle.setText(title);
				}
			});
		}
	}
}
