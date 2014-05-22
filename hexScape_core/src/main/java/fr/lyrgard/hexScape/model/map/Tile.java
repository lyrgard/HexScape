package fr.lyrgard.hexScape.model.map;

import java.util.HashMap;
import java.util.Map;

import fr.lyrgard.hexScape.model.Direction;



public class Tile {

	private TileType type;
	
	private int x,y,z;
	
	public Tile(TileType type, int x, int y , int z) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private Map<Direction, Tile> neighbours = new HashMap<Direction, Tile>();
	
	
	public Map<Direction, Tile> getNeighbours() {
		return neighbours;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		Tile other = (Tile) obj;
		if (type != other.type)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	
	
}
