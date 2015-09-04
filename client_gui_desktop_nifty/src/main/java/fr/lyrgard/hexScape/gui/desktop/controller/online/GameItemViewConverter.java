package fr.lyrgard.hexScape.gui.desktop.controller.online;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import fr.lyrgard.hexScape.model.game.Game;

public class GameItemViewConverter implements ListBoxViewConverter<Game> {

	@Override
	public void display(Element listBoxItem, Game game) {
		GameItemController gameItemController = listBoxItem.getControl(GameItemController.class);
		gameItemController.setGame(game);
	}

	@Override
	public int getWidth(Element element, Game item) {
		return 250;
	}

}
