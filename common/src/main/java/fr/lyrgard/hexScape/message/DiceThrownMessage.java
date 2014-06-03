package fr.lyrgard.hexScape.message;

import java.util.List;

public class DiceThrownMessage extends AbstractMessage {

	private String diceTypeId;
	
	private List<String> results;

	public DiceThrownMessage(String playerId, String diceTypeId,
			List<String> results) {
		super(playerId);
		this.diceTypeId = diceTypeId;
		this.results = results;
	}

	public String getDiceTypeId() {
		return diceTypeId;
	}

	public List<String> getResults() {
		return results;
	}
	
	
}
