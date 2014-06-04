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
import fr.lyrgard.hexScape.model.ChatTypeEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

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
		
		// TODO MessageBus.register(this);
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
//	@Subscribe public void onRoomMessageReceived(RoomMessagePostedEvent event) {
//		Player player = event.getPlayer();
//		String message = event.getMessage();
//		chatPanel.addMessage(player, message);
//	}
//	
//	@Subscribe public void onPlayerJoined(PlayerJoinedRoomEvent event) {
//		Player player = event.getPlayer();
//		playerListModel.addPlayer(player);
//		chatPanel.addAction("player " + player.getName() + " joined the room");
//	}
//	
//	@Subscribe public void onPlayerLeft(PlayerLeftRoomEvent event) {
//		Player player = event.getPlayer();
//		chatPanel.addAction("player " + player.getName() + " left the room");
//		playerListModel.removePlayer(player);
//	}

	@Override
	public void refresh() {
		// TODOHexScapeCore.getInstance().getMultiplayerService().requestRoomContent(Room.DEFAULT_ROOM_ID);
	}
	
	
}
