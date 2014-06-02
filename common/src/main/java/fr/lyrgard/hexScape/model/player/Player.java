package fr.lyrgard.hexScape.model.player;

import java.awt.Color;

import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.room.Room;

public class Player {
	
	private String id;
	
	private String name;
	
	private Color color;
	
	private CardCollection army;
	
	private Room room;
	
	private Game game;

	public Player() {
	}
	
	public Player(String name, Color color) {
		super();
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public CardCollection getArmy() {
		return army;
	}

	public void setArmy(CardCollection army) {
		this.army = army;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return name;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
