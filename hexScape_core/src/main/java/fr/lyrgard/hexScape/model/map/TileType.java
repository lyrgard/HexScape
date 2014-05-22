package fr.lyrgard.hexScape.model.map;

import fr.lyrgard.hexScape.model.model3d.TileTexture;

public enum TileType {
	GRASS(TileTexture.GRASS, TileTexture.GROUND_SIDE), 
	ROCK(TileTexture.ROCK, TileTexture.GROUND_SIDE), 
	SAND(TileTexture.SAND, TileTexture.GROUND_SIDE), 
	WATER(TileTexture.WATER, TileTexture.WATER_SIDE, true),
	SWAMP(TileTexture.SWAMP, TileTexture.SWAMP_SIDE), 
	SWAMP_WATER(TileTexture.SWAMP_WATER, TileTexture.SWAMP_WATER_SIDE, true),
	ROAD(TileTexture.ROAD, TileTexture.ROAD_SIDE),
	LAVA_FIELD(TileTexture.LAVA_FIELD, TileTexture.LAVA_FIELD_SIDE), 
	MOLTEN_LAVA(TileTexture.MOLTEN_LAVA, TileTexture.MOLTEN_LAVA_SIDE, true),
	SNOW(TileTexture.SNOW, TileTexture.SNOW_SIDE), 
	ICE(TileTexture.ICE, TileTexture.ICE_SIDE, true),
	DUNGEON(TileTexture.DUNGEON, TileTexture.DUNGEON_SIDE), 
	SHADOW(TileTexture.SHADOW, TileTexture.SHADOW_SIDE, true),
	CONCRETE(TileTexture.CONCRETE, TileTexture.CONCRETE_SIDE), 
	ASPHALT(TileTexture.ASPHALT, TileTexture.ASPHALT_SIDE);
	
	
	private TileTexture topTexture;
	private TileTexture sideTexture;
	private boolean halfTile;
	
	private TileType(TileTexture topTexture, TileTexture sideTexture) {
		this.topTexture = topTexture;
		this.sideTexture = sideTexture;
	}
	
	private TileType(TileTexture topTexture, TileTexture sideTexture, boolean halfTile) {
		this.topTexture = topTexture;
		this.sideTexture = sideTexture;
		this.halfTile = halfTile;
	}

	public TileTexture getTopTexture() {
		return topTexture;
	}

	public TileTexture getSideTexture() {
		return sideTexture;
	}
	
	public boolean isHalfTile() {
		return halfTile;
	}
}
