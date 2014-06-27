package fr.lyrgard.hexScape.service;

import fr.lyrgard.hexScape.model.player.ColorEnum;

public class ColorService {

	private static final ColorService INSTANCE = new ColorService();
	
	public static ColorService getInstance() {
		return INSTANCE;
	}
	
	private ColorService() {
	}
	
	private int currentOrdinal = 0; 
	
	public ColorEnum getNextColorThatIsNot(ColorEnum color) {
		nextOrdinal();
		if (currentOrdinal == color.ordinal()) {
			nextOrdinal();
		}
		return ColorEnum.values()[currentOrdinal];
	}
	
	private void nextOrdinal() {
		currentOrdinal = (currentOrdinal + 1) % ColorEnum.values().length;
	}
}
