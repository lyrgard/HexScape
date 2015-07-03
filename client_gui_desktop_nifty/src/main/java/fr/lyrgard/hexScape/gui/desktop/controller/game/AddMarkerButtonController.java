package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.service.MarkerService;

public class AddMarkerButtonController extends AbstractImageButtonController {

	public static final String MARKER_TYPE_ID = "markerTypeId";
	public static final String CARD_ID = "cardId";
	
	@Override
	public void onClick() {
		String markerTypeId = attributes.get(MARKER_TYPE_ID);
		String cardId = attributes.get(CARD_ID);
	
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerTypeId);
		if (markerDefinition != null) {
			boolean stackable = (markerDefinition.getType() == MarkerType.STACKABLE);
			String hiddenMarkerTypeId = null;
			String markerTypeIdToPlace = markerTypeId;
			if (markerDefinition instanceof RevealableMarkerDefinition) {
				// We place a marker of type HiddenMarker that hide the marker we wanted to place
				hiddenMarkerTypeId = markerTypeId;
				markerTypeIdToPlace = ((RevealableMarkerDefinition)markerDefinition).getHiddenMarkerDefinition().getId();
			}
			PlaceMarkerMessage message = new PlaceMarkerMessage(cardId, markerTypeIdToPlace, 1, hiddenMarkerTypeId, stackable);
			CoreMessageBus.post(message);
		}
	}

}
