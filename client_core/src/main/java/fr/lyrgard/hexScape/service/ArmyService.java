package fr.lyrgard.hexScape.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;

public class ArmyService {
	
	private static final ArmyService INSTANCE = new ArmyService();
	
	public static ArmyService getInstance() {
		return INSTANCE;
	}
	
	private ArmyService() {
	}
	
	public Army loadArmy(String playerId, File armyFile) {
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
							CoreMessageBus.post(new ErrorMessage(playerId, "Error in " + armyFile + " army file line " + lineNumber + ". \"" + numberString + "\" is not a valid number"));
							return null;
						}
						cardId = tokens[1];
					}
				}
				
				CardType cardType = CardService.getInstance().getCardInventory().getCardsById().get(cardId);
				
				if (cardType == null) {
					if (firstLine) {
						army.setName(line);
					} else {
						CoreMessageBus.post(new ErrorMessage(playerId, "Error in " + armyFile + " army file line " + lineNumber + ". \"" + cardId + "\" card was not found in the inventory"));
						return null;
					}
				} else {
					String cardInstanceId = playerId + "-" + i; 
					CardInstance cardInstance = new CardInstance(cardInstanceId, cardType.getId(), number);
					army.getCards().add(cardInstance);
					i++;
				}
				
				firstLine = false;
			}
		} catch (FileNotFoundException e) {
			CoreMessageBus.post(new ErrorMessage(playerId, "The army file " + armyFile + " does not exists"));
		} catch (IOException e) {
			CoreMessageBus.post(new ErrorMessage(playerId, "The army file " + armyFile + " does not exists"));
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return army;
	}
}
