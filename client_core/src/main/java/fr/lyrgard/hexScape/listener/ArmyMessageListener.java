package fr.lyrgard.hexScape.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.LoadArmyMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.CardService;

public class ArmyMessageListener {
	
	private static ArmyMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ArmyMessageListener();
			MessageBus.register(instance);
		}
	}
	
	private ArmyMessageListener() {
	}

	@Subscribe public void onLoadArmyMessage(LoadArmyMessage message) {
		File armyFile = message.getArmyFile();
		String playerId = message.getPlayerId();
		Army army = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(armyFile));
			String line;
			int lineNumber = 0;
			boolean firstLine = true;
			
			army = new Army();
			int i = 0;
			
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (StringUtils.isBlank(line)) {
					continue;
				}
				
				int number = 1;
				String cardId = null;
				
				line = line.trim();
				String[] tokens = line.split("\\s");
				if (tokens.length == 1) {
					cardId = line;
				} else if (tokens.length == 2) {
					if (tokens[0].endsWith("x")) {
						String numberString = tokens[0].substring(0, tokens[0].length() - 1);
						try {
							number = Integer.parseInt(numberString);
						} catch (NumberFormatException e) {
							MessageBus.post(new ErrorMessage(playerId, "Error in " + armyFile + " army file line " + lineNumber + ". \"" + numberString + "\" is not a valid number"));
							return;
						}
						cardId = tokens[1];
					}
				}
				
				CardType cardType = CardService.getInstance().getCardInventory().getCardsById().get(cardId);
				
				if (cardType == null) {
					if (firstLine) {
						army.setName(line);
					} else {
						MessageBus.post(new ErrorMessage(playerId, "Error in " + armyFile + " army file line " + lineNumber + ". \"" + cardId + "\" card was not found in the inventory"));
						return;
					}
				} else {
					String cardInstanceId = playerId + "-" + i; 
					CardInstance cardInstance = new CardInstance(cardInstanceId, cardType, number);
					army.getCardsById().put(cardInstance.getId(), cardInstance);
					i++;
				}
				
				firstLine = false;
			}
		} catch (FileNotFoundException e) {
			MessageBus.post(new ErrorMessage(playerId, "The army file " + armyFile + " does not exists"));
		} catch (IOException e) {
			MessageBus.post(new ErrorMessage(playerId, "The army file " + armyFile + " does not exists"));
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null) {
			player.setArmy(army);
			MessageBus.post(new ArmyLoadedMessage(playerId, army));
		}
		
	}

}
