package fr.lyrgard.hexScape.gui.desktop.controller.online;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.gui.desktop.controller.chat.ChatEntryModel;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.ChatEntryModel.ChatEntryType;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;

public class RoomChatController {

	private HexScapeChatControl roomChat;


	public RoomChatController(Room room, HexScapeChatControl gameChat) {
		super();
		this.roomChat = gameChat;

		gameChat.clear();

		for (User user : room.getUsers()) {
			gameChat.addUser(user);
		}
	}

	private void displayUserMessage(User user, String message) {
		roomChat.receivedChatLine(new ChatEntryModel(user, ChatEntryType.MESSAGE, message));
	}

	private void displayUserAction(User user, String action) {
		roomChat.receivedChatLine(new ChatEntryModel(user, ChatEntryType.ACTION, action));
	}

	@Subscribe public void onChatMessage(RoomMessagePostedMessage message) {
		String userId = message.getSessionUserId();
		String messageContent = message.getMessage();


		User user = Universe.getInstance().getUsersByIds().get(userId);
		if (user != null) {
			displayUserMessage(user, messageContent);
		}
	}

	@Subscribe public void onRoomLeft(DisconnectedFromServerMessage message) {
		final String userId = message.getUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		if (user != null) {
			displayUserAction(user, "left the room");
			roomChat.removeUser(user);
		}
	}


	@Subscribe public void onRoomJoined(UserJoinedRoomMessage message) {
		final String userId = message.getUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null) {
			displayUserAction(user, "joined the game");
			roomChat.addUser(user);
		}
	}
}
