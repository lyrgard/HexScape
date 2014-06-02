package fr.lyrgard.hexScape.event;

import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.player.Player;

public class ArmyLoadedEvent {

	private CardCollection army;
	
	private Player player;

	public ArmyLoadedEvent(CardCollection army, Player player) {
		super();
		this.army = army;
		this.player = player;
	}

	public CardCollection getArmy() {
		return army;
	}

	public Player getPlayer() {
		return player;
	}
}
