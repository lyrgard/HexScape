package fr.lyrgard.hexScape.model.player;

import java.awt.Color;

public enum ColorEnum {
	BLUE(Color.BLUE),
	RED(Color.RED),
	GREEN(Color.GREEN);
	
	private Color color;

	private ColorEnum(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
}
