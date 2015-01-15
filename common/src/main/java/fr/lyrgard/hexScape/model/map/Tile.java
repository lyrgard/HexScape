package fr.lyrgard.hexScape.model.map;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tile {
	
	private int x,y,z;
	
	private boolean startZone;
	
	private int startZoneNumber;
	
	private boolean halfSize;
	
	private int topTexture;
	
	private int sideTexture;
	
	private boolean visible;
	
	@JsonCreator
	public Tile(
			@JsonProperty("x") int x, 
			@JsonProperty("y") int y , 
			@JsonProperty("z") int z,
			@JsonProperty("halfSize") boolean halfSize,
			@JsonProperty("topTexture") int topTexture , 
			@JsonProperty("sideTexture") int sideTexture,
			@JsonProperty("visible") boolean visible,
			@JsonProperty("startZone") boolean startZone , 
			@JsonProperty("startZoneNumber") int startZoneNumber) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.halfSize = halfSize;
		this.topTexture = topTexture;
		this.sideTexture = sideTexture;
		this.visible = visible;
		this.startZone = startZone;
		this.startZoneNumber = startZoneNumber;
	}
	
	@JsonIgnore
	private Map<Direction, Tile> neighbours = new HashMap<Direction, Tile>();
	
	
	public Map<Direction, Tile> getNeighbours() {
		return neighbours;
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

	

	public boolean isStartZone() {
		return startZone;
	}

	public void setStartZone(boolean startZone) {
		this.startZone = startZone;
	}

	public int getStartZoneNumber() {
		return startZoneNumber;
	}

	public void setStartZoneNumber(int startZoneNumber) {
		this.startZoneNumber = startZoneNumber;
	}

	public boolean isHalfSize() {
		return halfSize;
	}

	public void setHalfSize(boolean halfSize) {
		this.halfSize = halfSize;
	}


	public int getTopTexture() {
		return topTexture;
	}


	public void setTopTexture(int topTexture) {
		this.topTexture = topTexture;
	}


	public int getSideTexture() {
		return sideTexture;
	}


	public void setSideTexture(int sideTexture) {
		this.sideTexture = sideTexture;
	}

	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (visible ? 1231 : 1237);
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
		if (visible != other.visible)
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
