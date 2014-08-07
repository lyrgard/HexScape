package fr.lyrgard.hexScape.model.player;

import java.awt.Color;

public enum ColorEnum {
	BLUE(Color.BLUE),
	RED(Color.RED),
	GREEN(Color.GREEN),
	CYAN(Color.CYAN),
	PINK(Color.PINK),
	ORANGE(Color.ORANGE);
	
	private Color color;

	private ColorEnum(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
}
