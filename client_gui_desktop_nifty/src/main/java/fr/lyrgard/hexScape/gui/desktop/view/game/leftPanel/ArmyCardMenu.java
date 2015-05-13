package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.MarkerService;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.AddMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.AddStackableMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.ChangeCardOwnerAction;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseMapAction;
import fr.lyrgard.hexScape.gui.desktop.action.RemoveAllMarkersOfTypeForPlayerdAction;

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
		
		for (final MarkerDefinition markerType : markerTypes) {
			if (markerType instanceof HiddenMarkerDefinition) {
				JMenuItem removeMarkersItem = new JMenuItem(new RemoveAllMarkersOfTypeForPlayerdAction(markerType));
				add(removeMarkersItem);
			}
		}
		
		Game game = CurrentUserInfo.getInstance().getGame();
		if (game != null) {
			if (game.getPlayers().size() > 1) {
				final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/share.png"));
				JMenu giveCardToNewOwnerMenu = new JMenu("give this card to another player :");
				giveCardToNewOwnerMenu.setIcon(icon);
				for (Player player : game.getPlayers()) {
					if (player.getArmy() != null && !player.getArmy().hasCard(card.getId())) {
						JMenuItem giveCardToPlayerItem = new JMenuItem(new ChangeCardOwnerAction(card, player));
						giveCardToPlayerItem.setText(player.getDisplayName());
						giveCardToNewOwnerMenu.add(giveCardToPlayerItem);
					}
				}
				add(giveCardToNewOwnerMenu);
			}
		}
	}
}
