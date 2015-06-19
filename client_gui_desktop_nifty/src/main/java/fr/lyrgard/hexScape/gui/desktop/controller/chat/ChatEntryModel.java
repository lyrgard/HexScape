package fr.lyrgard.hexScape.gui.desktop.controller.chat;

import java.util.List;

import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;

public class ChatEntryModel {
	
	public static enum ChatEntryType {
		MESSAGE,
		ACTION,
		DICE_ROLL;
	}
	
	private Player player;
	
	private User user;
	
	public DiceType getDiceType() {
		return diceType;
	}

	public List<Integer> getDiceResults() {
		return diceResults;
	}

	private ChatEntryType type;
	
	private String message;
	
	private DiceType diceType;
	
	private List<Integer> diceResults;

	public ChatEntryModel(Player player, ChatEntryType type, String message) {
		super();
		this.player = player;
		this.type = type;
		this.message = message;
	}
	
	public ChatEntryModel(User user, ChatEntryType type, String message) {
		super();
		this.user = user;
		this.type = type;
		this.message = message;
	}
	
	public ChatEntryModel(Player player, DiceType diceType, List<Integer> diceResults) {
		super();
		this.player = player;
		this.diceType = diceType;
		this.diceResults = diceResults;
		this.type = ChatEntryType.DICE_ROLL;
	}

	public Player getPlayer() {
		return player;
	}

	public ChatEntryType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

}
