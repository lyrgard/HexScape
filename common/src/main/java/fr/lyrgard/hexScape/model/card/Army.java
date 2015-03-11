package fr.lyrgard.hexScape.model.card;

import java.util.ArrayList;
import java.util.List;

public class Army {

	private String name;
	
	private List<CardInstance> cards = new ArrayList<CardInstance>();
	
	
	public void addCard(CardInstance card) {
		cards.add(card);
	}
	
	public CardInstance getCard(String cardId) {
		for (CardInstance card : cards) {
			if (card.getId().equals(cardId)) {
				return card;
			}
		}
		return null;
	}
	
	public boolean hasCard(String cardId) {
		for (CardInstance card : cards) {
			if (card.getId().equals(cardId)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CardInstance> getCards() {
		return cards;
	}

}
