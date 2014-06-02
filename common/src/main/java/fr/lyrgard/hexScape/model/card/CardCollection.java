package fr.lyrgard.hexScape.model.card;

import java.util.HashMap;
import java.util.Map;


public class CardCollection {

	private String name;
	
	private Map<String, Card> cardsById = new HashMap<String, Card>();
	
	private Map<String, Integer> numberById = new HashMap<String, Integer>();
	
	private Map<String, Card> cardsByPieceId = new HashMap<String, Card>();
	
	public void addCard(String id, Card card, Integer number) {
		cardsById.put(id, card);
		numberById.put(id, number);
		for (String pieceId : card.getFigureNames()) {
			cardsByPieceId.put(pieceId, card);
		}
	}

	public Map<String, Card> getCardsById() {
		return cardsById;
	}

	public Map<String, Integer> getNumberById() {
		return numberById;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Card> getCardsByPieceId() {
		return cardsByPieceId;
	}
	
}
