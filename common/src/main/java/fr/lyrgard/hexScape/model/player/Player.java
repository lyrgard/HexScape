package fr.lyrgard.hexScape.model.player;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.room.Room;

public class Player {
	
	private String id;
	
	private String name;
	
	private ColorEnum color;
	
	@JsonIgnore
	private Army army;
	
	@JsonIgnore
	private Room room;
	
	@JsonIgnore
	private Game game;
	
	@JsonIgnore
	private Map<String, PieceInstance> piecesById = new HashMap<>(); 

	public Player() {
	}
	
	public Player(String name, ColorEnum color) {
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

	public ColorEnum getColor() {
		return color;
	}

	public void setColor(ColorEnum color) {
		this.color = color;
	}

	public Army getArmy() {
		return army;
	}

	public void setArmy(Army army) {
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

	public Map<String, PieceInstance> getPiecesById() {
		return piecesById;
	}
}
