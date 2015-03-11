package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
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
		String userId = message.getSessionUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);

		if (user != null) {
			Room room = user.getRoom();


			if (room != null) {
				room.getUsers().remove(user);
				user.setRoom(null);

				DisconnectedFromServerMessage returnMessage = new DisconnectedFromServerMessage(userId);
				ServerNetwork.getInstance().sendMessageToRoomExceptUser(returnMessage, room.getId(), userId);

				if (user.getGame() != null) {
					Game game = user.getGame();
					Player player = game.getPlayerByUserId(userId);
					String playerId = null;
					
					if (player != null) {
						player.setUserId(null);
						user.setGameId(null);
						user.setPlayer(null);
						playerId = player.getId();
					}
					
					GameLeftMessage returnMessage2 = new GameLeftMessage(user.getId(), playerId, game.getId());
					ServerNetwork.getInstance().sendMessageToRoomExceptUser(returnMessage2, room.getId(), user.getId());

					if (game.getFreePlayers().size() == game.getPlayers().size()) {
						// Empty game, remove
						room.getGames().remove(game);
						Universe.getInstance().getGamesByGameIds().remove(game.getId());
						GameEndedMessage returnMessage3 = new GameEndedMessage(game.getId());
						ServerNetwork.getInstance().sendMessageToRoomExceptUser(returnMessage3, room.getId(), userId);
					}
				}
			}

			Universe.getInstance().getUsersByIds().remove(userId);
		}
	}
}
