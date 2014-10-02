package fr.lyrgard.hexScape.server.service;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.UnknownTypeMarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class GameService {
	
	public static void removeUnseeableHiddenMarkersInfos(Game game, String userId) {

		for (Player player : game.getPlayers()) {
			if (player != null && player.getArmy() != null) {
				for (CardInstance card : player.getArmy().getCards()) {
					for (MarkerInstance marker : card.getMarkers()) {
						if (marker instanceof UnknownTypeMarkerInstance) {
							if (!StringUtils.equals(userId, player.getUserId())) {
								((UnknownTypeMarkerInstance) marker).setHiddenMarkerTypeId(null);
							}
						}
					}
				}
			}
		}

	}
}
