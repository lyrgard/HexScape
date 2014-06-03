package fr.lyrgard.hexScape.message;

public class MarkerRevealedMessage extends AbstractMarkerMessage {

	public MarkerRevealedMessage(String playerId, String gameId, String cardId, String markerId, int number) {
		super(playerId, gameId, cardId, markerId, number);
	}

}
