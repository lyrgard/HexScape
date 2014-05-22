package fr.lyrgard.hexScape.gui.desktop.components.menuComponent;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.gui.desktop.action.AddMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.AddStackableMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.RemoveAllMarkersFromCardAction;

public class ArmyCardMenu extends JPopupMenu {

	private static final long serialVersionUID = 7054882230582407223L;

	public ArmyCardMenu(final Card card) {
		Collection<MarkerDefinition> markers = HexScapeCore.getInstance().getMarkerService().getMarkersListForCard();
		
		for (final MarkerDefinition marker : markers) {
			
			
			if (marker.getType() == MarkerType.STACKABLE) {
				final ImageIcon icon = new ImageIcon(marker.getImage().getAbsolutePath());
				JMenu addStackableMarkerMenu = new JMenu("add/remove " + marker.getName() + " to this card");
				addStackableMarkerMenu.setIcon(icon);
				for (int i = 1; i <= 10; i++) {
					JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(marker, card, i));
					addStackableMarkerMenu.add(numberItem);
				}
				for (int i = -1; i >= -10; i--) {
					JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(marker, card, i));
					addStackableMarkerMenu.add(numberItem);
				}
				add(addStackableMarkerMenu);
			} else {
				JMenuItem addMarkerItem = new JMenuItem(new AddMarkerToCardAction(marker, card));
				add(addMarkerItem);
			}
		}
		
		if (!markers.isEmpty()) {
			JMenuItem removeMarkersItem = new JMenuItem(new RemoveAllMarkersFromCardAction(card));
			add(removeMarkersItem);
		}
	}
}
