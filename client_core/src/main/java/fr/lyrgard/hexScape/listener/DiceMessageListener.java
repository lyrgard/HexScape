package fr.lyrgard.hexScape.listener;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

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
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String playerId = message.getPlayerId();
			String diceTypeId = message.getDiceTypeId();
			int number = message.getNumber();

			DiceType type = DiceService.getInstance().getDiceType(diceTypeId);

			if (type == null) {
				CoreMessageBus.post(new ErrorMessage(playerId, "The dice type \"" + diceTypeId + "\" was not found"));
			} else {
				List<Integer> results = new ArrayList<>();
				for (int i = 0; i < number; i++) {
					results.add(roll(type.getFaces().size()));
				}
				CoreMessageBus.post(new DiceThrownMessage(playerId, diceTypeId, results));
			}
		}
	}

	private int roll(int numberOfFace) {
		return (int)(Math.random() * (numberOfFace)); 
	}
	
	@Subscribe public void onDiceThrownMessage(DiceThrownMessage message) {
		GuiMessageBus.post(message);
	}
}
