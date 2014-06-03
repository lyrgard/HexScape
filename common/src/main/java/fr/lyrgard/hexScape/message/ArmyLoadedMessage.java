package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.card.CardCollection;

public class ArmyLoadedMessage extends AbstractMessage {
	private CardCollection army;

	public ArmyLoadedMessage(String playerId, CardCollection army) {
		super(playerId);
		this.army = army;
	}
	
	public CardCollection getArmy() {
		return army;
	}
}
