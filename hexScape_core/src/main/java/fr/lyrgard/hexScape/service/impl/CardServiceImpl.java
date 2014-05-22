package fr.lyrgard.hexScape.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ArmyLoadedEvent;
import fr.lyrgard.hexScape.event.ErrorEvent;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.CardCollection;
import fr.lyrgard.hexScape.service.CardService;

public class CardServiceImpl implements CardService {
	
	private static final File baseFolder = new File("asset/cards");
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String FIGURES_3D = "3dFigures";
	
	private static final String cardPropertiesFilename = "card.properties";
	
	private CardCollection cardInventory;

	@Override
	public CardCollection loadCardInventory(File baseFolder) {
		if (cardInventory != null) {
			return cardInventory;
		}
		if (baseFolder == null) {
			baseFolder = CardServiceImpl.baseFolder;
		}
		cardInventory = new CardCollection();
		if (baseFolder.exists()) {
			for (File folder : baseFolder.listFiles()) {
				if (folder.exists() && folder.isDirectory()) {
					File cardPropertiesFile = new File(folder, cardPropertiesFilename);
					if (cardPropertiesFile.exists() && cardPropertiesFile.isFile() && cardPropertiesFile.canRead()) {
						Card card = new Card();
						Properties cardProperties = new Properties();
						InputStream input;
						try {
							input = new FileInputStream(cardPropertiesFile);
							cardProperties.load(input);
							
							String id =  cardProperties.getProperty(ID);
							card.setId(id);
							card.setName(cardProperties.getProperty(NAME));
							card.setFolder(folder);
							String figures3d = cardProperties.getProperty(FIGURES_3D);
							String[] figures = figures3d.split(",");
							for (String figureName : figures) {
								figureName = figureName.trim();
								card.getFigureNames().add(figureName);
							}
							cardInventory.addCard(id, card, null);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		return cardInventory;
	}

	@Override
	public void loadArmy(File armyFile) {
		CardCollection army = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(armyFile));
			String line;
			int lineNumber = 0;
			boolean firstLine = true;
			
			army = new CardCollection();
			
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
							HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("Error in " + armyFile + " army file line " + lineNumber + ". \"" + numberString + "\" is not a valid number"));
							return;
						}
						cardId = tokens[1];
					}
				}
				
				Card card = cardInventory.getCardsById().get(cardId);
				
				if (card == null) {
					if (firstLine) {
						army.setName(line);
					} else {
						HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("Error in " + armyFile + " army file line " + lineNumber + ". \"" + cardId + "\" card was not found in the inventory"));
						return;
					}
				} else {
					army.addCard(cardId, card, number);
				}
				
				firstLine = false;
			}
		} catch (FileNotFoundException e) {
			HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("The army file " + armyFile + " does not exists"));
		} catch (IOException e) {
			HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("The army file " + armyFile + " does not exists"));
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		HexScapeCore.getInstance().getEventBus().post(new ArmyLoadedEvent(army, HexScapeCore.getInstance().getPlayer()));
	}

	@Override
	public Card getCardByPieceId(String pieceId) {
		return cardInventory.getCardsByPieceId().get(pieceId);
	}

}
