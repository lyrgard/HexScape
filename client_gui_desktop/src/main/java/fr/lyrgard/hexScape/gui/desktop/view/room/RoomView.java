package fr.lyrgard.hexScape.gui.desktop.view.room;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.DisconnectAction;
import fr.lyrgard.hexScape.gui.desktop.components.chatComponent.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.components.room.GameListPanel;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.model.ChatTypeEnum;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.service.RoomService;

public class RoomView extends AbstractView {

	private static final long serialVersionUID = 2920503440331541048L;

	private JLabel roomTitle = new JLabel();
	
	private Room room;
	
	private PlayerListModel playerListModel;
	
	private ChatPanel chatPanel;
	
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
		
		chatPanel = new ChatPanel(HexScapeCore.getInstance().getRoomId(), null);
		add(chatPanel, BorderLayout.CENTER);
		
		MessageBus.register(this);
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
		String name = message.getName();
		ColorEnum color = message.getColor();
		
		
		if (!HexScapeCore.getInstance().getPlayerId().equals(playerId)) {
			Player player = new Player(name, color);
			player.setId(playerId);
			
			RoomService.getInstance().playerJoinedRoom(player);

			if (player != null) {
				playerListModel.addPlayer(player);
				chatPanel.addAction("player " + player.getName() + " joined the room");
			}
		}
	}
//	
//	@Subscribe public void onPlayerLeft(PlayerLeftRoomEvent event) {
//		Player player = event.getPlayer();
//		chatPanel.addAction("player " + player.getName() + " left the room");
//		playerListModel.removePlayer(player);
//	}

	@Override
	public void refresh() {
		String roomId = HexScapeCore.getInstance().getRoomId();
		
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (room != null) {
			chatPanel.setRoomId(roomId);
			roomTitle.setText(room.getName());
			playerListModel.setPlayers(room.getPlayers());
			chatPanel.addAction("Room " + room.getName() + " joined.");
		}
	}
	
	
}
