package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.gui.desktop.action.AddStackableMarkerToCardAction;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.service.MarkerService;

public class StackableMarkerMenu extends JPopupMenu {

	private static final long serialVersionUID = 2503273763767899214L;

	public StackableMarkerMenu(CardInstance card, StackableMarkerInstance marker) {
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());
		for (int i = 1; i <= 10; i++) {
			JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(markerDefinition, card, i));
			add(numberItem);
		}
		for (int i = -1; i >= -10; i--) {
			JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(markerDefinition, card, i));
			add(numberItem);
		}
	}
}
