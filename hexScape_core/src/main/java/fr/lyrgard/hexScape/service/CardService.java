package fr.lyrgard.hexScape.service;

import java.io.File;

import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.CardCollection;

public interface CardService {

	CardCollection loadCardInventory(File folder);
	
	void loadArmy(File armyFile);
	
	Card getCardByPieceId(String pieceId);
}
