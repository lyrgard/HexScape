package fr.lyrgard.hexScape.message;

public class RevealMarkerMessage extends AbstractMarkerMessage {

	public RevealMarkerMessage(String playerId, String gameId, String cardId, String markerId, int number) {
		super(playerId, gameId, cardId, markerId, number);
	}

}
