package fr.lyrgard.hexScape.gui.desktop.view.room;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.DisconnectAction;
import fr.lyrgard.hexScape.gui.desktop.message.GameSelectedMessage;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.gui.desktop.view.common.HexaFont;
import fr.lyrgard.hexScape.gui.desktop.view.common.chat.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.view.room.games.GameListPanel;
import fr.lyrgard.hexScape.gui.desktop.view.room.games.SelectedGamePanel;
import fr.lyrgard.hexScape.gui.desktop.view.room.users.UserList;
import fr.lyrgard.hexScape.gui.desktop.view.room.users.UserListModel;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;

public class RoomView extends AbstractView {

	private static final long serialVersionUID = 2920503440331541048L;

	private JLabel roomTitle = new JLabel();

	private UserListModel userListModel;

	private ChatPanel chatPanel;

	private JPanel centerPanel;

	private SelectedGamePanel selectedGamePanel = new SelectedGamePanel();

	public RoomView() {
		setLayout(new BorderLayout());

		roomTitle.setFont(HexaFont.getFont().deriveFont(40f));

		JPanel topRow = new JPanel();
		topRow.add(roomTitle);
		topRow.add(new JButton(new DisconnectAction()));
		add(topRow, BorderLayout.PAGE_START);


		add(new GameListPanel(), BorderLayout.LINE_START);

		JPanel playersPanel = new JPanel(new BorderLayout());
		playersPanel.setPreferredSize(new Dimension(200, 400));
		JLabel playersTitle = new JLabel("PLAYERS");
		playersTitle.setFont(HexaFont.getFont().deriveFont(35f));
		playersTitle.setHorizontalAlignment(SwingConstants.CENTER);
		playersPanel.add(playersTitle, BorderLayout.PAGE_START);

		userListModel = new UserListModel();
		UserList userList = new UserList(userListModel);
		userList.setPreferredSize(new Dimension(200, 400));
		playersPanel.add(userList, BorderLayout.CENTER);
		add(playersPanel, BorderLayout.LINE_END);

		centerPanel = new JPanel(new BorderLayout());
		chatPanel = new ChatPanel(null, null);
		centerPanel.add(chatPanel, BorderLayout.CENTER);

		selectedGamePanel.setVisible(false);
		centerPanel.add(selectedGamePanel, BorderLayout.PAGE_END);

		add(centerPanel, BorderLayout.CENTER);

		GuiMessageBus.register(this);
	}

	public void setRoom(Room room) {
		chatPanel.clearText();
		chatPanel.setRoomId(room.getId());
		roomTitle.setText(room.getName());
		userListModel.setUsers(room.getUsers());
		chatPanel.addAction("Room " + room.getName() + " joined.");
	}

	// TODO
	//	@Subscribe public void onRoomContentReceived(RoomContentReceivedEvent event) {
	//		room = event.getRoom();
	//		roomTitle.setText(room.getName());
	//		playerListModel.setPlayers(room.getPlayers());
	//		chatPanel.addAction("Room " + room.getName() + " joined.");
	//		
	//	}
	//	
	@Subscribe public void onRoomJoined(RoomJoinedMessage message) {
		final Room room = message.getRoom();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				RoomView.this.setRoom(room);
				HexScapeFrame.getInstance().showView(ViewEnum.ROOM);
			}
		});
	}

	@Subscribe public void onRoomMessageReceived(RoomMessagePostedMessage message) {
		String userId = message.getUserId();
		String roomId = message.getRoomId();
		final String messageContent = message.getMessage();

		if (CurrentUserInfo.getInstance().getRoomId().equals(roomId)) {
			final User user = Universe.getInstance().getUsersByIds().get(userId);

			if (user != null) {
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						chatPanel.addMessage(user, messageContent);
					}
				});
			}
		}

	}

	@Subscribe public void onPlayerJoined(UserJoinedRoomMessage message) {
		String userId = message.getUserId();

		final User user = Universe.getInstance().getUsersByIds().get(userId);

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (user != null) {
					userListModel.addUser(user);
					chatPanel.addAction("player " + user.getName() + " joined the room");
				}
			}
		});
	}

	@Subscribe public void onPlayerLeftRoom(DisconnectedFromServerMessage message) {
		final String userId = message.getUserId();

		if (!CurrentUserInfo.getInstance().getId().equals(userId)) {

			EventQueue.invokeLater(new Runnable() {

				public void run() {
					User user = Universe.getInstance().getUsersByIds().get(userId);

					if (user != null) {
						chatPanel.addAction("player " + user.getName() + " left the room");
						userListModel.removeUser(user);
					}
				}
			});
		}
	}

	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		final String userId = message.getUserId();
		final Game game = message.getGame();

		if (HexScapeCore.getInstance().isOnline()) {
			if (game != null) {

				EventQueue.invokeLater(new Runnable() {

					public void run() {
						if (CurrentUserInfo.getInstance().getId().equals(userId)) {
							GuiMessageBus.post(new GameSelectedMessage(game));
							selectedGamePanel.setVisible(true);
						} 

						User user = Universe.getInstance().getUsersByIds().get(userId);

						if (user != null) {
							chatPanel.addAction("player " + user.getName() + " created game " + game.getName());
						}
						userListModel.redraw();
					}

				});
			}

		}
	}

	@Subscribe public void gameJoined(GameJoinedMessage message) {
		final Game game = message.getGame();
		final String userId = message.getUserId();
		

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (!userId.equals(CurrentUserInfo.getInstance().getId())) {
					User user = Universe.getInstance().getUsersByIds().get(userId);

					if (game != null) {
						chatPanel.addAction("player " + user.getName() + " joined game " + game.getName());
					}
				}
				userListModel.redraw();
			}
		});
	}

	@Subscribe public void onGameLeft(GameLeftMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				userListModel.redraw();
			}
		});
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();
		
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
				if (game != null) {
					chatPanel.addAction("game " + game.getName() + " started");
				}
				userListModel.redraw();
			}
		});
	}

	@Override
	public void refresh() {
		String roomId = CurrentUserInfo.getInstance().getRoomId();

		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);

		if (room != null) {

		}
	}


}
