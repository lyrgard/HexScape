package fr.lyrgard.hexScape.model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Map {
	
	private String name;
	
	private java.util.Map<Integer, java.util.Map<Integer, java.util.Map<Integer, Tile>>> tiles = new TreeMap<Integer, java.util.Map<Integer,java.util.Map<Integer,Tile>>>();
	
	private List<Decor> decors = new ArrayList<>();
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.util.Map<Integer, java.util.Map<Integer, java.util.Map<Integer, Tile>>> getTiles() {
		return tiles;
	}

	public List<Decor> getDecors() {
		return decors;
	}
	
	public Tile getTile(int x, int y, int z) {
		Tile tile = null;
		java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ = tiles.get(z);
		if (byZ != null) {
			java.util.Map<Integer, Tile> byY = byZ.get(y);
			if (byY != null) {
				tile = byY.get(x);
			}
		}
		return tile;
	}
}

