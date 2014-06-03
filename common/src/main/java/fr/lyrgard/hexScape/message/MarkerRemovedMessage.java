package fr.lyrgard.hexScape.message;

public class MarkerRemovedMessage extends AbstractMarkerMessage {
	
	private boolean allMarkers;

	public MarkerRemovedMessage(String playerId, String gameId, String cardId, String markerId, int number, boolean allMarkers) {
		super(playerId, gameId, cardId, markerId, number);
		this.allMarkers = allMarkers;
	}

	public boolean isAllMarkers() {
		return allMarkers;
	} 

}
