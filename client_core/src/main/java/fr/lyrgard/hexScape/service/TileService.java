package fr.lyrgard.hexScape.service;

import java.util.Map;

import fr.lyrgard.hexScape.model.map.TileType;
import fr.lyrgard.hexScape.model.model3d.TileTexture;

public class TileService {

	private static final TileService INSTANCE = new TileService();

	public static TileService getInstance() {
		return INSTANCE;
	}

	private Map<TileType, TileTexture> topTextures;

	private Map<TileType, TileTexture> sideTextures;
	
	private Map<TileType, Boolean> halfSizes;

	private TileService() {
		topTextures.put(TileType.GRASS,TileTexture.GRASS); 
		topTextures.put(TileType.ROCK,TileTexture.ROCK); 
		topTextures.put(TileType.SAND,TileTexture.SAND);
		topTextures.put(TileType.WATER,TileTexture.WATER);
		topTextures.put(TileType.SWAMP,TileTexture.SWAMP); 
		topTextures.put(TileType.SWAMP_WATER,TileTexture.SWAMP_WATER);
		topTextures.put(TileType.ROAD,TileTexture.ROAD);
		topTextures.put(TileType.LAVA_FIELD,TileTexture.LAVA_FIELD);
		topTextures.put(TileType.MOLTEN_LAVA,TileTexture.MOLTEN_LAVA);
		topTextures.put(TileType.SNOW,TileTexture.SNOW);
		topTextures.put(TileType.ICE,TileTexture.ICE);
		topTextures.put(TileType.DUNGEON,TileTexture.DUNGEON);
		topTextures.put(TileType.SHADOW,TileTexture.SHADOW);
		topTextures.put(TileType.CONCRETE,TileTexture.CONCRETE); 
		topTextures.put(TileType.ASPHALT,TileTexture.ASPHALT);

		sideTextures.put(TileType.GRASS,TileTexture.GRASS); 
		sideTextures.put(TileType.ROCK,TileTexture.ROCK); 
		sideTextures.put(TileType.SAND,TileTexture.SAND);
		sideTextures.put(TileType.WATER,TileTexture.WATER);
		sideTextures.put(TileType.SWAMP,TileTexture.SWAMP);
		sideTextures.put(TileType.SWAMP_WATER,TileTexture.SWAMP_WATER);
		sideTextures.put(TileType.ROAD,TileTexture.ROAD);
		sideTextures.put(TileType.LAVA_FIELD,TileTexture.LAVA_FIELD); 
		sideTextures.put(TileType.MOLTEN_LAVA,TileTexture.MOLTEN_LAVA);
		sideTextures.put(TileType.SNOW,TileTexture.SNOW);
		sideTextures.put(TileType.ICE,TileTexture.ICE);
		sideTextures.put(TileType.DUNGEON,TileTexture.DUNGEON);
		sideTextures.put(TileType.SHADOW,TileTexture.SHADOW);
		sideTextures.put(TileType.CONCRETE,TileTexture.CONCRETE);
		sideTextures.put(TileType.ASPHALT,TileTexture.ASPHALT);
		
		halfSizes.put(TileType.GRASS,Boolean.FALSE); 
		halfSizes.put(TileType.ROCK,Boolean.FALSE); 
		halfSizes.put(TileType.SAND,Boolean.FALSE);
		halfSizes.put(TileType.WATER,Boolean.TRUE);
		halfSizes.put(TileType.SWAMP,Boolean.FALSE);
		halfSizes.put(TileType.SWAMP_WATER,Boolean.TRUE);
		halfSizes.put(TileType.ROAD,Boolean.FALSE);
		halfSizes.put(TileType.LAVA_FIELD,Boolean.FALSE); 
		halfSizes.put(TileType.MOLTEN_LAVA,Boolean.TRUE);
		halfSizes.put(TileType.SNOW,Boolean.FALSE);
		halfSizes.put(TileType.ICE,Boolean.TRUE);
		halfSizes.put(TileType.DUNGEON,Boolean.FALSE);
		halfSizes.put(TileType.SHADOW,Boolean.TRUE);
		halfSizes.put(TileType.CONCRETE,Boolean.FALSE);
		halfSizes.put(TileType.ASPHALT,Boolean.FALSE);
	}
	
	public TileTexture getTopTexture(TileType tileType) {
		return topTextures.get(tileType);
	}

	public TileTexture getSideTexture(TileType tileType) {
		return sideTextures.get(tileType);
	}
	
	public boolean isHalfTile(TileType tileType) {
		return halfSizes.get(tileType);
	}
}
