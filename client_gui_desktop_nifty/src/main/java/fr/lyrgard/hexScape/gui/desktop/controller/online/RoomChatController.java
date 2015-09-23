package fr.lyrgard.hexScape.gui.desktop.controller.online;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.gui.desktop.controller.chat.ChatEntryModel;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.ChatEntryModel.ChatEntryType;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
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
	
	@Subscribe public void gameJoined(GameJoinedMessage message) {
		final Game game = message.getGame();
		final String userId = message.getUserId();
		
		if (!userId.equals(CurrentUserInfo.getInstance().getId())) {
			User user = Universe.getInstance().getUsersByIds().get(userId);

			if (user != null && game != null) {
				displayUserAction(user, " joined " + game.getName());
			}
		}
	}

	@Subscribe public void onGameLeft(GameLeftMessage message) {
		final String gameId = message.getGameId();
		final String userId = message.getUserId();
		
		if (!userId.equals(CurrentUserInfo.getInstance().getId())) {
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			User user = Universe.getInstance().getUsersByIds().get(userId);

			if (user != null && game != null) {
				displayUserAction(user, " left " + game.getName());
			}
		}
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();
		final String userId = message.getUserId();
		

		if (!userId.equals(CurrentUserInfo.getInstance().getId())) {
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			User user = Universe.getInstance().getUsersByIds().get(userId);

			if (user != null && game != null) {
				displayUserAction(user, " started " + game.getName());
			}
		}
	}
}
