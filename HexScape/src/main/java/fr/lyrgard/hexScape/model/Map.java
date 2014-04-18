package fr.lyrgard.hexScape.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Map {

	private java.util.Map<Integer, java.util.Map<Integer, java.util.Map<Integer, Tile>>> tiles = new TreeMap<Integer, java.util.Map<Integer,java.util.Map<Integer,Tile>>>();
	
	private List<Decor> decors = new ArrayList<>();


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

	public Tile getFirstTile() {
		Tile tile = null;
		outerloop: for (Entry<Integer, java.util.Map<Integer, java.util.Map<Integer, Tile>>> entryZ : tiles.entrySet()) {
			for (Entry<Integer, java.util.Map<Integer, Tile>> entryY : entryZ.getValue().entrySet()) {
				for (Entry<Integer, Tile> entryX : entryY.getValue().entrySet()) {
					tile = entryX.getValue();
					if (tile != null) {
						break outerloop;
					}
				}
			}
		}
		return tile;
	}

	public List<Decor> getDecors() {
		return decors;
	}
}
