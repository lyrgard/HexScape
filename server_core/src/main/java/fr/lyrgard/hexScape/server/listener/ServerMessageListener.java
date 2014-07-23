package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexscape.server.network.ServerNetwork;

public class ServerMessageListener {

	private static ServerMessageListener instance;

	public static void start() {
		if (instance == null) {
			instance = new ServerMessageListener();
			CoreMessageBus.register(instance);
		}
	}

	private ServerMessageListener() {
	}

	@Subscribe public void onDisconnect(DisconnectedFromServerMessage message) {
		String playerId = message.getPlayerId();

		Player player = Universe.getInstance().getPlayersByIds().get(playerId);

		if (player != null) {
			Room room = player.getRoom();


			if (room != null) {
				room.getPlayers().remove(player);
				player.setRoom(null);

				DisconnectedFromServerMessage returnMessage = new DisconnectedFromServerMessage(playerId);
				ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(returnMessage, room.getId(), playerId);

				if (player.getGameId() != null) {
					Game game = Universe.getInstance().getGamesByGameIds().get(player.getGameId());
					game.getPlayersIds().remove(playerId);
					player.setGameId(null);
					GameLeftMessage returnMessage2 = new GameLeftMessage(playerId, game.getId());
					ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(returnMessage2, room.getId(), playerId);

					if (game.getPlayersIds().isEmpty()) {
						room.getGames().remove(game);
						Universe.getInstance().getGamesByGameIds().remove(game.getId());
						GameEndedMessage returnMessage3 = new GameEndedMessage(game.getId());
						ServerNetwork.getInstance().sendMessageToRoomExceptPlayer(returnMessage3, room.getId(), playerId);
					}
				}
			}

			Universe.getInstance().getPlayersByIds().remove(playerId);
		}
	}
}
