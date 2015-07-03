package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.Iterator;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.message.RemoveMarkerMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.MarkerService;

public class RemoveAllMarkerOfMarkerTypeButtonController extends AbstractImageButtonController {

public static final String MARKER_TYPE_ID = "markerTypeId";
	
	@Override
	public void onClick() {
		String markerTypeId = attributes.get(MARKER_TYPE_ID);
		
		MarkerDefinition markerType = MarkerService.getInstance().getMarkersByIds().get(markerTypeId);
	
		String playerId = CurrentUserInfo.getInstance().getPlayerId();
		Game game = CurrentUserInfo.getInstance().getGame();

		if (game != null && markerType != null) {
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
