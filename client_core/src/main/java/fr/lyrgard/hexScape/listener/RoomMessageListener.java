package fr.lyrgard.hexScape.listener;

import java.util.Iterator;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.service.ColorService;
import fr.lyrgard.hexScape.service.MarkerService;

public class RoomMessageListener {

	private static RoomMessageListener INSTANCE = new RoomMessageListener();
	
	public static void start() {
		CoreMessageBus.register(INSTANCE);
	}
	
	private RoomMessageListener() {
	}
	
	@Subscribe public void onRoomJoined(RoomJoinedMessage message) {
		Room room = message.getRoom();
		Universe.getInstance().getRoomsByRoomIds().put(room.getId(), room);
		
		User user = Universe.getInstance().getUsersByIds().get(CurrentUserInfo.getInstance().getId());
		
		CurrentUserInfo.getInstance().setRoom(room);
		
		Iterator<User> it = room.getUsers().iterator();
		while (it.hasNext()) {
			User otherUser = it.next();
			if (!otherUser.getId().equals(CurrentUserInfo.getInstance().getId())) {
				if (otherUser.getColor() == user.getColor()) {
					otherUser.setColor(ColorService.getInstance().getNextColorThatIsNot(user.getColor()));
				}
				otherUser.setRoom(room);
				Universe.getInstance().getUsersByIds().put(otherUser.getId(), otherUser);
			} else {
				// We need to replace ourself in the list by our own Player object (already created)
				// so remove the player to is ourself
				it.remove();
			}
		}
		// add our own Player object
		room.getUsers().add(user);
		for (Game game : room.getGames()) {
			MarkerService.getInstance().normalizeMarkers(game);
			Universe.getInstance().getGamesByGameIds().put(game.getId(), game);
			for (Player player : game.getPlayers()) {
				if (player.getUserId() != null) {
					User aUser = Universe.getInstance().getUsersByIds().get(player.getUserId());
					aUser.setPlayer(player);
					aUser.setGame(game);
				}
			}
		}
		
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onPlayerJoindedRoom(UserJoinedRoomMessage message) {
		String userId = message.getUserId();
		String name = message.getName();
		ColorEnum color = message.getColor();
		
		User user = Universe.getInstance().getUsersByIds().get(CurrentUserInfo.getInstance().getId());
		
		User joiningUser = new User();
		joiningUser.setId(userId);
		joiningUser.setName(name);
		joiningUser.setColor(color);

		String roomId = CurrentUserInfo.getInstance().getRoomId();
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		if (room != null) {
			if (joiningUser.getColor() == user.getColor()) {
				joiningUser.setColor(ColorService.getInstance().getNextColorThatIsNot(user.getColor()));
			}
			Universe.getInstance().getUsersByIds().put(joiningUser.getId(), joiningUser);
			room.getUsers().add(joiningUser);
		}

		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onRoomLeft(DisconnectedFromServerMessage message) {
		GuiMessageBus.post(message);
	}
}
