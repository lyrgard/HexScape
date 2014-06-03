package fr.lyrgard.hexScape.message;

public class PlaceMarkerMessage extends AbstractMarkerMessage {

	public PlaceMarkerMessage(String playerId, String gameId, String cardId, String markerId, int number) {
		super(playerId, gameId, cardId, markerId, number);
	}

	
}
