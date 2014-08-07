package fr.lyrgard.hexScape.listener;

import java.util.Iterator;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.service.ColorService;

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
		
		Player player = Universe.getInstance().getPlayersByIds().get(HexScapeCore.getInstance().getPlayerId());
		if (player != null) {
			player.setRoom(room);
		}
		
		Iterator<Player> it = room.getPlayers().iterator();
		while (it.hasNext()) {
			Player otherPlayers = it.next();
			if (!otherPlayers.getId().equals(HexScapeCore.getInstance().getPlayerId())) {
				if (otherPlayers.getColor() == player.getColor()) {
					otherPlayers.setColor(ColorService.getInstance().getNextColorThatIsNot(player.getColor()));
				}
				Universe.getInstance().getPlayersByIds().put(otherPlayers.getId(), otherPlayers);
			} else {
				// We need to replace ourself in the list by our own Player object (already created)
				// so remove the player to is ourself
				it.remove();
			}
		}
		// add our own Player object
		room.getPlayers().add(player);
		for (Game game : room.getGames()) {
			Universe.getInstance().getGamesByGameIds().put(game.getId(), game);
		}
		
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onPlayerJoindedRoom(PlayerJoinedRoomMessage message) {
		String playerId = message.getPlayerId();
		String name = message.getName();
		ColorEnum color = message.getColor();
		
		Player player = Universe.getInstance().getPlayersByIds().get(HexScapeCore.getInstance().getPlayerId());
		
		Player joiningPlayer = new Player(name, color);
		joiningPlayer.setId(playerId);

		String roomId = HexScapeCore.getInstance().getRoomId();
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		if (room != null) {
			if (joiningPlayer.getColor() == player.getColor()) {
				joiningPlayer.setColor(ColorService.getInstance().getNextColorThatIsNot(player.getColor()));
			}
			Universe.getInstance().getPlayersByIds().put(joiningPlayer.getId(), joiningPlayer);
			room.getPlayers().add(joiningPlayer);
		}

		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onRoomLeft(DisconnectedFromServerMessage message) {
		GuiMessageBus.post(message);
	}
}
