package fr.lyrgard.hexScape.listener;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;

public class DiceMessageLocalListener {

	private static DiceMessageLocalListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new DiceMessageLocalListener();
			MessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			MessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private DiceMessageLocalListener() {
	}

	@Subscribe public void onThrowDiceMessage(ThrowDiceMessage message) {
		String playerId = message.getPlayerId();
		String diceTypeId = message.getDiceTypeId();
		int number = message.getNumber();
		
		DiceType type = DiceService.getInstance().getDiceType(diceTypeId);
		
		if (type == null) {
			MessageBus.post(new ErrorMessage(playerId, "The dice type \"" + diceTypeId + "\" was not found"));
		} else {
			List<String> results = new ArrayList<>();
			for (int i = 0; i < number; i++) {
				results.add(type.getFaces().get(roll(type)).getId());
			}
			MessageBus.post(new DiceThrownMessage(playerId, diceTypeId, results));
		}
	}

	private int roll(DiceType type) {
		return (int)(Math.random() * (type.getFaces().size())); 
	}
	
	
}
