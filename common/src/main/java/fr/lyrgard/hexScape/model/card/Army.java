package fr.lyrgard.hexScape.model.card;

import java.util.HashMap;
import java.util.Map;

public class Army {

	private String name;
	
	private Map<String, CardInstance> cardsById = new HashMap<String, CardInstance>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, CardInstance> getCardsById() {
		return cardsById;
	}

}
