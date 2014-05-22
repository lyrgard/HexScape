package fr.lyrgard.hexScape.event.marker;

import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.Player;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;

public class MarkersOnCardChangedEvent {

	private Player player;
	
	private Card card;
	
	private MarkerInstance marker;
	
	private int number;

	public MarkersOnCardChangedEvent(Player player, Card card, MarkerInstance marker, int number) {
		super();
		this.player = player;
		this.card = card;
		this.marker = marker;
		this.number = number;
	}

	public Card getCard() {
		return card;
	}

	public MarkerInstance getMarker() {
		return marker;
	}

	public int getNumber() {
		return number;
	}

	public Player getPlayer() {
		return player;
	}
	
	
}