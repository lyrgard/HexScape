package fr.lyrgard.hexScape.io.virtualScape.bean;

public enum TileType {
	
	GRASS(1,0,false,true),
	SAND(2,0, false,true),
	ROCK(3,0, false,true),
	WATER(4,5, true,true),
	SWAMP(6,7, false,true),
	SWAMP_WATER(8,9,true,true),
	ROAD(10,11, false,true),
	LAVA_FIELD(12,13,false,true),
	MOLTEN_LAVA(14,15,true,true),
	SNOW(16,17,false,true),
	ICE(18,19,true,true),
	DUNGEON(20,21,false,true),
	SHADOW(22,23,true,true),
	CONCRETE(24,25,false,true),
	ASPHALT(26,27,false,true),
	INVISIBLE(-1,-1,false,false);
	
	private int topTexture;
	private int sideTexture;
	private boolean halfSize;
	private boolean visible;
	
	private TileType(int topTexture, int sideTexture, boolean halfSize, boolean visible) {
		this.topTexture = topTexture;
		this.sideTexture = sideTexture;
		this.halfSize = halfSize;
		this.visible = visible;
	}
	public int getTopTexture() {
		return topTexture;
	}
	public int getSideTexture() {
		return sideTexture;
	}
	public boolean isHalfSize() {
		return halfSize;
	}
	public boolean isVisible() {
		return visible;
	}
	
	
	
}
