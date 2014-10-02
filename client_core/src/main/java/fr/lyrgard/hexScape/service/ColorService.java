package fr.lyrgard.hexScape.service;

import fr.lyrgard.hexScape.model.player.ColorEnum;

public class ColorService {

	private static ColorService INSTANCE;
	
	public static synchronized ColorService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ColorService();
		}
		return INSTANCE;
	}
	
	private ColorService() {
	}
	
	private int currentOrdinal = 0; 
	
	public ColorEnum getNextColorThatIsNot(ColorEnum color) {
		nextOrdinal();
		if (color != null && currentOrdinal == color.ordinal()) {
			nextOrdinal();
		}
		return ColorEnum.values()[currentOrdinal];
	}
	
	private void nextOrdinal() {
		currentOrdinal = (currentOrdinal + 1) % ColorEnum.values().length;
	}
}
