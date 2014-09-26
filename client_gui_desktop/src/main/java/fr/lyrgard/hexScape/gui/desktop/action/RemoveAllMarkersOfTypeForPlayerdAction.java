package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.RemoveMarkerMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class RemoveAllMarkersOfTypeForPlayerdAction extends AbstractAction {

	private static final long serialVersionUID = 2139602334359226593L;

	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/remove.png"));

	private MarkerDefinition markerType;


	public RemoveAllMarkersOfTypeForPlayerdAction(MarkerDefinition markerType) {
		super("Remove all " + markerType.getName() + " from your cards", icon);
		this.markerType = markerType;
	}


	public void actionPerformed(ActionEvent e) {
		String playerId = CurrentUserInfo.getInstance().getPlayerId();
		String gameId = CurrentUserInfo.getInstance().getGameId();

		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);

		if (game != null) {
			Player player = game.getPlayer(playerId);

			if (player != null) {

				for (CardInstance card : player.getArmy().getCards()) {
					Iterator<MarkerInstance> it = card.getMarkers().iterator();
					while (it.hasNext()) {
						MarkerInstance marker = it.next();
						boolean toRemove = false;
						if (markerType.getId().equals(marker.getMarkerDefinitionId())) {
							toRemove= true;
						} else if (markerType instanceof HiddenMarkerDefinition) {
							for (MarkerDefinition type : ((HiddenMarkerDefinition)markerType).getPossibleMarkersHidden()) {
								if (type.getId().equals(marker.getMarkerDefinitionId())) {
									toRemove = true;
									break;
								}
							}
						}

						if (toRemove) {
							int number = 1;
							if (marker instanceof StackableMarkerInstance) {
								number = ((StackableMarkerInstance)marker).getNumber();
							}
							it.remove();
							RemoveMarkerMessage message = new RemoveMarkerMessage(card.getId(), marker.getId(), number);
							CoreMessageBus.post(message);
						}
					}
				}
			}
		}
	}
}
