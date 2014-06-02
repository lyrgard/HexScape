package fr.lyrgard.hexScape.model.model3d;

import com.jme3.math.Vector2f;

public enum TileTexture {
	GROUND_SIDE,
	GRASS,
	SAND,
	ROCK,
	WATER,
	WATER_SIDE,
	SWAMP,
	SWAMP_SIDE,
	SWAMP_WATER,
	SWAMP_WATER_SIDE,
	ROAD,
	ROAD_SIDE,
	LAVA_FIELD,
	LAVA_FIELD_SIDE,
	MOLTEN_LAVA,
	MOLTEN_LAVA_SIDE,
	SNOW,
	SNOW_SIDE,
	ICE,
	ICE_SIDE,
	DUNGEON,
	DUNGEON_SIDE,
	SHADOW,
	SHADOW_SIDE,
	CONCRETE,
	CONCRETE_SIDE,
	ASPHALT,
	ASPHALT_SIDE;
	
	private int number;
	private float offset;
	
	public Vector2f getCoord(float x, float y) {
		if (number == 0) {
			number = TileTexture.values().length;
			offset = ordinal() / (float)number;
		}
		float newX = x / number + offset;
		float newY = y;
		return new Vector2f(newX, newY);
	}
}
