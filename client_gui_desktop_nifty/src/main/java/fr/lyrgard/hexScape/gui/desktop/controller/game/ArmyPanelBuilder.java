package fr.lyrgard.hexScape.gui.desktop.controller.game;

import de.lessvoid.nifty.builder.ControlBuilder;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class ArmyPanelBuilder extends ControlBuilder {

	public ArmyPanelBuilder(CardInstance cardInstance, String playerId, int place) {
		super("armyCardPanel_" + playerId + "_" + place, "armyCardPanel");
	}
}
