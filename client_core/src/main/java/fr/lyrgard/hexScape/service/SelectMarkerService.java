package fr.lyrgard.hexScape.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.lyrgard.hexScape.model.SelectMarker;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;

public class SelectMarkerService {

	private static final SelectMarkerService INSTANCE = new SelectMarkerService();
	
	public static SelectMarkerService getInstance() {
		return INSTANCE;
	}
	
	private SelectMarkerService() {
	}
	
	private Map<String, SelectMarker> markerByPlayerIds = new HashMap<String, SelectMarker>();
	
	public SelectMarker getSelectMarker(String playerId) {
		SelectMarker selectMarker = markerByPlayerIds.get(playerId);
		if (selectMarker == null) {
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);
			
			if (player != null) {
				selectMarker = new SelectMarker(player.getColor());
				markerByPlayerIds.put(playerId, selectMarker);
			}
		}
		
		return selectMarker;
	}
	
	public Collection<SelectMarker> getSelectMarkers() {
		return markerByPlayerIds.values();
	}
}
