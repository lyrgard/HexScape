package fr.lyrgard.hexScape.gui.desktop.view.room;

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
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.DisconnectAction;
import fr.lyrgard.hexScape.gui.desktop.components.chatComponent.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.components.game.SelectedGamePanel;
import fr.lyrgard.hexScape.gui.desktop.components.player.PlayerCellRenderer;
import fr.lyrgard.hexScape.gui.desktop.components.player.PlayerList;
import fr.lyrgard.hexScape.gui.desktop.components.room.GameListPanel;
import fr.lyrgard.hexScape.gui.desktop.message.GameSelectedMessage;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.RoomLeftMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

public class RoomView extends AbstractView {

	private static final long serialVersionUID = 2920503440331541048L;

	private JLabel roomTitle = new JLabel();

	private PlayerListModel playerListModel;

	private ChatPanel chatPanel;

	private JPanel centerPanel;

	private SelectedGamePanel selectedGamePanel = new SelectedGamePanel();

	public RoomView() {
		setLayout(new BorderLayout());

		JPanel topRow = new JPanel();
		topRow.add(roomTitle);
		topRow.add(new JButton(new DisconnectAction()));
		add(topRow, BorderLayout.PAGE_START);


		add(new GameListPanel(), BorderLayout.LINE_START);

		playerListModel = new PlayerListModel();
		PlayerList playerList = new PlayerList(playerListModel);
		playerList.setPreferredSize(new Dimension(200, 400));
		add(playerList, BorderLayout.LINE_END);

		centerPanel = new JPanel(new BorderLayout());
		chatPanel = new ChatPanel(HexScapeCore.getInstance().getRoomId(), null);
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
		playerListModel.setPlayers(room.getPlayers());
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

	@Subscribe public void onRoomMessageReceived(MessagePostedMessage message) {
		String playerId = message.getPlayerId();
		String roomId = message.getRoomId();
		String messageContent = message.getMessage();

		if (HexScapeCore.getInstance().getRoomId().equals(roomId)) {
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);

			if (player != null) {
				chatPanel.addMessage(player, messageContent);
			}
		}

	}

	@Subscribe public void onPlayerJoined(PlayerJoinedRoomMessage message) {
		String playerId = message.getPlayerId();

		Player player = Universe.getInstance().getPlayersByIds().get(playerId);

		if (player != null) {
			playerListModel.addPlayer(player);
			chatPanel.addAction("player " + player.getName() + " joined the room");
		}
	}

	@Subscribe public void onPlayerLeftRoom(DisconnectedFromServerMessage message) {
		String playerId = message.getPlayerId();
		if (!HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);

			if (player != null) {
				chatPanel.addAction("player " + player.getName() + " left the room");
				playerListModel.removePlayer(player);
			}
		}
	}

	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		final String playerId = message.getPlayerId();
		final Game game = message.getGame();

		if (game != null) {

			EventQueue.invokeLater(new Runnable() {

				public void run() {
					if (HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
						GuiMessageBus.post(new GameSelectedMessage(game));
						selectedGamePanel.setVisible(true);
					} 
					
					Player player = Universe.getInstance().getPlayersByIds().get(playerId);

					if (player != null) {
						chatPanel.addAction("player " + player.getName() + " created game " + game.getName());
					}
				}
			});
		} 
	}
	
	@Subscribe public void gameJoined(GameJoinedMessage message) {
		
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();

		if (!playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);
			
			if (player != null && player.getGame() != null && player.getGame().getId().equals(gameId)) {
				Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
				if (game != null) {
					chatPanel.addAction("player " + player.getName() + " joined game " + game.getName());
				}
			}
		}
	}

	@Override
	public void refresh() {
		String roomId = HexScapeCore.getInstance().getRoomId();

		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);

		if (room != null) {

		}
	}


}
