package fr.lyrgard.hexScape.gui.desktop.view.room.games;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.JoinGameAction;
import fr.lyrgard.hexScape.gui.desktop.action.LeaveGameAction;
import fr.lyrgard.hexScape.gui.desktop.action.ObserveGameAction;
import fr.lyrgard.hexScape.gui.desktop.action.StartGameAction;
import fr.lyrgard.hexScape.gui.desktop.message.GameSelectedMessage;
import fr.lyrgard.hexScape.gui.desktop.view.common.View3d;
import fr.lyrgard.hexScape.gui.desktop.view.common.newGame.PlayerCellRenderer;
import fr.lyrgard.hexScape.gui.desktop.view.common.newGame.PlayerListModel;
import fr.lyrgard.hexScape.gui.desktop.view.room.ActivateOnlineService;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
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
	
	private JButton leaveButton;

	private JButton observeButton;

	public SelectedGamePanel() {

		setLayout(new BorderLayout());

		gameTitle.setFont(gameTitle.getFont().deriveFont(25f));
		gameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(gameTitle, BorderLayout.PAGE_START);

		playerListModel = new PlayerListModel();
		JList<Player> playerList = new JList<Player>(playerListModel);
		playerList.setCellRenderer(new PlayerCellRenderer());
		playerList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		playerList.setLayoutOrientation(JList.VERTICAL);
		playerList.setVisibleRowCount(0);
		JScrollPane playerListScroller = new JScrollPane(playerList);
		playerListScroller.setPreferredSize(new Dimension(200, 200));
		add(playerListScroller, BorderLayout.LINE_END);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	
		add(buttonPanel, BorderLayout.LINE_START);

		joinButton = new JButton();
		buttonPanel.add(joinButton);

		startButton = new JButton("Start");
		buttonPanel.add(startButton);
		
		leaveButton = new JButton("Leave");
		buttonPanel.add(leaveButton);
		
		observeButton = new JButton("Observe");
		buttonPanel.add(observeButton);
		
		setBorder(new EmptyBorder(5, 5, 5, 5));

		ActivateOnlineService.getInstance().register(this);
	}

	public Game getGame() {
		return game;
	}

	@Subscribe public void gameSelected(GameSelectedMessage message) {

		if (message.getGame() == null) {
			game = null;
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					setVisible(false);
				}
			});
		} else {
			if (game == null || !game.getId().equals(message.getGame().getId())) {
				game = message.getGame();
				EventQueue.invokeLater(new Runnable() {

			public void run() {
				Component view3d = HexScapeFrame.getInstance().getView3d().getComponent();
				view3d.setSize(new Dimension(50, 50));
				view3d.setPreferredSize(new Dimension(UNDEFINED_CONDITION, 150));
				add(view3d, BorderLayout.CENTER);
				System.out.println("SELECTED GAME VIEW TAKES 3D");

						DisplayMapMessage displayMapMessage = new DisplayMapMessage(game.getId(), false);
						CoreMessageBus.post(displayMapMessage);

						gameTitle.setText(getTitle());

						playerListModel.removeAllPlayers();
						Collection<Player> freePlayers = game.getFreePlayers();
						boolean canJoin = !freePlayers.isEmpty();
						boolean canStart = false;
						boolean isInTheGame = false;
						boolean canObserve = false;
						for (Player player : game.getPlayers()) {
							if (player != null) {
								playerListModel.addPlayer(player);
							}
							if (game.getId().equals(CurrentUserInfo.getInstance().getGameId()) && player.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
								canJoin = false;
								canStart = !game.isStarted();
								isInTheGame = true;
							}
						}
						if (canJoin) {
							if (CurrentUserInfo.getInstance().getGameId() != null) {
								canJoin = false;
							}
						}
						canObserve = !isInTheGame && game.isStarted();


						if (canJoin) {
							joinButton.setAction(new JoinGameAction(game.getId()));
						}
						joinButton.setVisible(canJoin);
						if (canStart) {
							startButton.setAction(new StartGameAction(game.getId()));
							leaveButton.setAction(new LeaveGameAction());
						}
						startButton.setVisible(canStart);
						leaveButton.setVisible(canStart);
						if (canObserve) {
							observeButton.setAction(new ObserveGameAction(game.getId()));
						}
						observeButton.setVisible(canObserve);

						setVisible(true);
					}
				});
			}
		}
		
	
		
	}

	private String getTitle() {
		String title = game.getName();
		if (game.isStarted()) {
			title += " - STARTED";
		} else if (game.getFreePlayers().size() > 0) {
			title += " - WAITING PLAYERS";
		} else {
			title += " - READY TO START";
		}
		return title;
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

		final Game game = message.getGame();
		final String playerId = message.getPlayerId();

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				if (SelectedGamePanel.this.game != null && StringUtils.equals(game.getId(), SelectedGamePanel.this.game.getId())) {
					Player player = game.getPlayer(playerId);

					if (player != null) {
						playerListModel.removeAllPlayers();
						for (Player p : game.getPlayers()) {
							playerListModel.addPlayer(p);
						}

						if (playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
							joinButton.setVisible(false);
							startButton.setAction(new StartGameAction(game.getId()));
							startButton.setVisible(true);
							leaveButton.setAction(new LeaveGameAction());
							leaveButton.setVisible(true);
							
						} else {
							if (game.getFreePlayers().isEmpty()) {
								joinButton.setVisible(false);
							}
						}
						gameTitle.setText(getTitle());
					}
				}

			}
		});
	}

	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();

		if (HexScapeCore.getInstance().isOnline()) {
			if (gameId != null && game != null && gameId.equals(game.getId())) {
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
						if (game != null) {
							if (game.getFreePlayers().isEmpty()) {
								joinButton.setVisible(false);
							}
							startButton.setVisible(false);
							leaveButton.setVisible(false);

							gameTitle.setText(getTitle());
						}
					}
				});
			}
		}
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		final String gameId = message.getGameId();
		final String playerId = message.getPlayerId();
		
		
		if (HexScapeCore.getInstance().isOnline()) {
			if (gameId != null && game != null && gameId.equals(game.getId())) {
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						Player player = game.getPlayer(playerId);
						if (player != null) {
							playerListModel.redraw();

							if (CurrentUserInfo.getInstance().getGame() == null) {
								// game null == we left the game or we were not on this game.
								startButton.setVisible(false);
								leaveButton.setVisible(false);
								joinButton.setAction(new JoinGameAction(gameId));
								joinButton.setVisible(true);
							}
						}
					}
				});
			}
		}
	}
}
