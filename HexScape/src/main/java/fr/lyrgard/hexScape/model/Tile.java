package fr.lyrgard.hexScape.model;

import java.util.HashMap;
import java.util.Map;



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
	
	
}
