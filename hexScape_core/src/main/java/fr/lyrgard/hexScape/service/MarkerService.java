package fr.lyrgard.hexScape.service;

import java.util.Collection;

import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;

public interface MarkerService {

	Collection<MarkerDefinition> getMarkersListForCard();
	
	void addMarkerToCard(Card card, String id);
	
	void addStackableMarkerToCard(Card card, String id, int number); 
	
	void removeMarkerFromCard(Card card, MarkerInstance marker);
	
	void removeAllMarkersFromCard(Card card);
	
	void revealMarkerOnCard(Card card, RevealableMarkerInstance instance);
}
