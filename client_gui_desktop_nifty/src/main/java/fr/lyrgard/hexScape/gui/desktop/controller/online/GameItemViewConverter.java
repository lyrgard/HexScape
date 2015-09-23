package fr.lyrgard.hexScape.gui.desktop.controller.online;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;

public class GameItemViewConverter implements ListBoxViewConverter<String> {

	@Override
	public void display(Element listBoxItem, String gameId) {
		GameItemController gameItemController = listBoxItem.getControl(GameItemController.class);
		gameItemController.setGame(gameId);
	}

	@Override
	public int getWidth(Element element, String item) {
		return 250;
	}

}
