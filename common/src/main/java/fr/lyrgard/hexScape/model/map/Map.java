package fr.lyrgard.hexScape.model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Map {
	
	public Map() {
	}
	
	@JsonCreator
	public Map(
			@JsonProperty("name") String name,
			@JsonProperty("tiles") java.util.Map<Integer, java.util.Map<Integer, java.util.Map<Integer, Tile>>> tiles,
			@JsonProperty("decors") List<Decor> decors) {
		this.name = name;
		this.decors = decors;
		
		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : tiles.values()) {
			for (java.util.Map<Integer, Tile> byY : byZ.values()) {
				for (Tile tile : byY.values()) {
					addTile(tile.getType(), tile.getX(), tile.getY(), tile.getZ());
				}
			}
		}
	}
	
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
	
	public void addTile(TileType type, int x, int y, int z) {

		Tile tile = new Tile(type, x, y, z);

		setTile(tile);

		Tile newNeighbour;

		// North-East

		newNeighbour = getTile(x, y+1, z);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.SOUTH_WEST, tile);
			tile.getNeighbours().put(Direction.NORTH_EAST, newNeighbour);
		}

		// East
		newNeighbour = getTile(x+1, y, z);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.WEST, tile);
			tile.getNeighbours().put(Direction.EAST, newNeighbour);
		}

		// South-East
		newNeighbour = getTile(x+1, y-1, z);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.NORTH_WEST, tile);
			tile.getNeighbours().put(Direction.SOUTH_EAST, newNeighbour);
		}

		// South-West
		newNeighbour = getTile(x, y-1, z);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.NORTH_EAST, tile);
			tile.getNeighbours().put(Direction.SOUTH_WEST, newNeighbour);
		}

		// West
		newNeighbour = getTile(x-1, y, z);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.EAST, tile);
			tile.getNeighbours().put(Direction.WEST, newNeighbour);
		}

		// North-West
		newNeighbour = getTile(x-1, y+1, z);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.SOUTH_EAST, tile);
			tile.getNeighbours().put(Direction.NORTH_WEST, newNeighbour);
		}

		// Top
		newNeighbour = getTile(x, y, z+1);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.BOTTOM, tile);
			tile.getNeighbours().put(Direction.TOP, newNeighbour);
		}

		// Bottom
		newNeighbour = getTile(x, y, z-1);
		if (newNeighbour != null) {
			newNeighbour.getNeighbours().put(Direction.TOP, tile);
			tile.getNeighbours().put(Direction.BOTTOM, newNeighbour);
		}
	}
	
	private void setTile(Tile tile) {
		int x = tile.getX();
		int y = tile.getY();
		int z = tile.getZ();
		java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ = tiles.get(z);
		if (byZ == null) {
			byZ = new HashMap<Integer, java.util.Map<Integer,Tile>>();
			tiles.put(z, byZ);
		}
		java.util.Map<Integer, Tile> byY = byZ.get(y);
		if (byY == null) {
			byY = new HashMap<Integer, Tile>();
			byZ.put(y, byY);
		}
		byY.put(x, tile);
	}
}

