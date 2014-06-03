package fr.lyrgard.hexScape.message;

public class RemoveMarkerMessage extends AbstractMarkerMessage {
	
	private boolean allMarkers;

	public RemoveMarkerMessage(String playerId, String gameId, String cardId, String markerId, int number, boolean allMarkers) {
		super(playerId, gameId, cardId, markerId, number);
		this.allMarkers = allMarkers;
	}

	public boolean isAllMarkers() {
		return allMarkers;
	} 

}
