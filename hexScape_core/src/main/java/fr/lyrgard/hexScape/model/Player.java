package fr.lyrgard.hexScape.model;

import java.awt.Color;

public class Player {
	
	private String name;
	
	private Color color;
	
	private CardCollection army;

	public Player(String name, Color color) {
		super();
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public CardCollection getArmy() {
		return army;
	}

	public void setArmy(CardCollection army) {
		this.army = army;
	}
}
