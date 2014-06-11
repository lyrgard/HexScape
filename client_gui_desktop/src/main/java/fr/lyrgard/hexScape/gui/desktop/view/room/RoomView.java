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
import fr.lyrgard.hexScape.gui.desktop.components.room.GameListPanel;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
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
	
	private String roomId;
	
	public RoomView() {
		setLayout(new BorderLayout());
		
		JPanel topRow = new JPanel();
		topRow.add(roomTitle);
		topRow.add(new JButton(new DisconnectAction()));
		add(topRow, BorderLayout.PAGE_START);
		
		add(new GameListPanel(), BorderLayout.LINE_START);
		
		playerListModel = new PlayerListModel();
		JList<Player> userList = new JList<Player>(playerListModel); 
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(0);
		JScrollPane userListScroller = new JScrollPane(userList);
		userListScroller.setPreferredSize(new Dimension(200, 400));
		add(userListScroller, BorderLayout.LINE_END);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		chatPanel = new ChatPanel(HexScapeCore.getInstance().getRoomId(), null);
		centerPanel.add(chatPanel, BorderLayout.CENTER);
		
		
		
		add(centerPanel, BorderLayout.CENTER);
		
		GuiMessageBus.register(this);
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
	
	public void setRoom(Room room) {
		chatPanel.clearText();
		chatPanel.setRoomId(room.getId());
		roomTitle.setText(room.getName());
		playerListModel.setPlayers(room.getPlayers());
		chatPanel.addAction("Room " + room.getName() + " joined.");
	}
	
	@Subscribe public void onPlayerLeftRoom(RoomLeftMessage message) {
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
		String playerId = message.getPlayerId();
		Game game = message.getGame();

		if (game != null) {
			if (!HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
				JoinedGamePanel joinedGame = new JoinedGamePanel(game);
			} else {
				// add it to the left list
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
