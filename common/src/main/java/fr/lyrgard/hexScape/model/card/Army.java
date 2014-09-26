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
		return cards.stream().filter(card -> card.getId().equals(cardId)).findFirst().orElse(null);
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
