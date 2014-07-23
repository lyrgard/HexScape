package fr.lyrgard.hexScape.server.listener;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexscape.server.network.ServerNetwork;

public class DiceMessageListener {

	private static DiceMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new DiceMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private DiceMessageListener() {
	}

	@Subscribe public void onThrowDiceMessage(ThrowDiceMessage message) {
		String playerId = message.getPlayerId();
		String diceTypeId = message.getDiceTypeId();
		int number = message.getNumber();
		int numberOfFaces = message.getNumberOfFaces();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);

		if (player != null && player.getGameId() != null) {
			List<Integer> results = new ArrayList<>();
			for (int i = 0; i < number; i++) {
				results.add(roll(numberOfFaces));
			}
			ServerNetwork.getInstance().sendMessageToGame(new DiceThrownMessage(playerId, diceTypeId, results), player.getGameId());
		}
	}


	private int roll(int numberOfFace) {
		return (int)(Math.random() * (numberOfFace)); 
	}

}
