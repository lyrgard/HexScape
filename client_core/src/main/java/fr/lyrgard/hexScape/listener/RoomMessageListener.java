package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.RoomLeftMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;

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
		
		for (Player otherPlayers : room.getPlayers()) {
			if (!otherPlayers.getId().equals(HexScapeCore.getInstance().getPlayerId())) {
				Universe.getInstance().getPlayersByIds().put(otherPlayers.getId(), otherPlayers);
			}
		}
		for (Game game : room.getGames()) {
			Universe.getInstance().getGamesByGameIds().put(game.getId(), game);
		}
		
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onPlayerJoindedRoom(PlayerJoinedRoomMessage message) {
		String playerId = message.getPlayerId();

		String name = message.getName();
		ColorEnum color = message.getColor();
		Player player = new Player(name, color);
		player.setId(playerId);

		String roomId = HexScapeCore.getInstance().getRoomId();
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		if (room != null) {
			Universe.getInstance().getPlayersByIds().put(player.getId(), player);
			room.getPlayers().add(player);
		}

		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onRoomLeft(DisconnectedFromServerMessage message) {
		GuiMessageBus.post(message);
	}
}
