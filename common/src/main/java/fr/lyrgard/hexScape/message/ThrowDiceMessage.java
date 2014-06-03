package fr.lyrgard.hexScape.message;

public class ThrowDiceMessage extends AbstractMessage {

	private int number;
	private String diceTypeId;
	
	
	public ThrowDiceMessage(String playerId, int number, String diceTypeId) {
		super(playerId);
		this.number = number;
		this.diceTypeId = diceTypeId;
	}
	
	public int getNumber() {
		return number;
	}
	public String getDiceTypeId() {
		return diceTypeId;
	}
}
