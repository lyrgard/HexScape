package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexscape.server.network.ServerNetwork;


public class RoomMessageListener  {

	private static RoomMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new RoomMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private RoomMessageListener() {
	}

	
	@Subscribe public void onJoinRoom(JoinRoomMessage message) {
		String playerId = message.getPlayerId();
		String roomId = message.getRoomId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		Room room = Universe.getInstance().getRoomsByRoomIds().get(roomId);
		
		if (player != null && room != null) {
			room.getPlayers().add(player);
			player.setRoom(room);
			
			RoomJoinedMessage returnMessage = new RoomJoinedMessage(room);
			ServerNetwork.getInstance().sendMessageToPlayer(returnMessage, playerId);
			
			PlayerJoinedRoomMessage broadcastMessage = new PlayerJoinedRoomMessage(playerId, player.getName(), player.getColor(), roomId);
			ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(broadcastMessage, roomId, playerId);
		}
	}
	
	@Subscribe public void onRoomLeft(DisconnectedFromServerMessage message) {
		String playerId = message.getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null) {
			Room room = player.getRoom();
			Game game = player.getGame();

			if (room != null) {
				room.getPlayers().remove(player);
				player.setRoom(null);

				DisconnectedFromServerMessage returnMessage = new DisconnectedFromServerMessage(playerId);
				ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(returnMessage, room.getId(), playerId);
				
				if (game != null) {
					game.getPlayersIds().remove(playerId);
					player.setGame(null);
					if (game.getPlayersIds().isEmpty()) {
						room.getGames().remove(game);
						Universe.getInstance().getGamesByGameIds().remove(game.getId());
						GameEndedMessage returnMessage2 = new GameEndedMessage(game.getId());
						ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(returnMessage2, room.getId(), playerId);
					}
				}
			}
			
			Universe.getInstance().getPlayersByIds().remove(playerId);
		}
	}
	
	@Subscribe public void onMessagePosted(MessagePostedMessage message) {
		String roomId = message.getRoomId();
		
		if (roomId != null) {
			ServerNetwork.getInstance().sendMessageToRoom(message, roomId);
		}
	}
}
