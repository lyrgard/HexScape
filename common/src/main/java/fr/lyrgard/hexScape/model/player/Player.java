package fr.lyrgard.hexScape.model.player;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.piece.PieceInstance;

public class Player {
	
	private String id;
	
	private String name;
	
	private ColorEnum color = ColorEnum.RED;
	
	private String userId;
	
	private Army army;
	
	private List<PieceInstance> pieces = new ArrayList<PieceInstance>(); 

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
	
	@JsonIgnore
	public String getDisplayName() {
		String result = name;
		if (userId != null) {
			User user = Universe.getInstance().getUsersByIds().get(userId);
			if (user != null) {
				result = user.getName() + " (" + result + ")";
			}
		} else {
			result += " (Empty)";
		}
		return result;
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
		return getDisplayName();
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

	public List<PieceInstance> getPieces() {
		return pieces;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
