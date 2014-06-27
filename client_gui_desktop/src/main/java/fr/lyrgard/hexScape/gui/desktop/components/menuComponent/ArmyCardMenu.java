package fr.lyrgard.hexScape.gui.desktop.components.menuComponent;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.service.MarkerService;
import fr.lyrgard.hexScape.gui.desktop.action.AddMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.AddStackableMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.RemoveAllMarkersFromCardAction;

public class ArmyCardMenu extends JPopupMenu {

	private static final long serialVersionUID = 7054882230582407223L;

	public ArmyCardMenu(final CardInstance card) {
		Collection<MarkerDefinition> markerTypes = MarkerService.getInstance().getMarkersListForCard();
		
		for (final MarkerDefinition markerType : markerTypes) {
			
			switch(markerType.getType()) {
			case STACKABLE:
				final ImageIcon icon = new ImageIcon(markerType.getImage().getAbsolutePath());
				JMenu addStackableMarkerMenu = new JMenu("add/remove " + markerType.getName() + " to this card");
				addStackableMarkerMenu.setIcon(icon);
				for (int i = 1; i <= 10; i++) {
					JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(markerType, card, i));
					addStackableMarkerMenu.add(numberItem);
				}
				for (int i = -1; i >= -10; i--) {
					JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(markerType, card, i));
					addStackableMarkerMenu.add(numberItem);
				}
				add(addStackableMarkerMenu);
				break;
			case REVEALABLE:
				add(new JMenuItem(new AddMarkerToCardAction(markerType, ((RevealableMarkerDefinition)markerType).getHiddenMarkerDefinition(), card)));
				break;
			case NORMAL:
				add(new JMenuItem(new AddMarkerToCardAction(markerType, null, card)));
				break;
			case HIDDEN:
				break;
			}
		}
		
		if (!markerTypes.isEmpty()) {
			JMenuItem removeMarkersItem = new JMenuItem(new RemoveAllMarkersFromCardAction(card));
			add(removeMarkersItem);
		}
	}
}
