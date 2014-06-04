package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.card.Army;

public class ArmyLoadedMessage extends AbstractMessage {
	private Army army;

	public ArmyLoadedMessage(String playerId, Army army) {
		super(playerId);
		this.army = army;
	}
	
	public Army getArmy() {
		return army;
	}
}
