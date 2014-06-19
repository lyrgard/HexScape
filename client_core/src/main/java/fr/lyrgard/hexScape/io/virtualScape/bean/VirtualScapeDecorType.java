package fr.lyrgard.hexScape.io.virtualScape.bean;

import java.util.HashMap;
import java.util.Map;

import fr.lyrgard.hexScape.model.map.Direction;


@SuppressWarnings("serial")
public enum VirtualScapeDecorType {
	RUIN_2("Ruin2", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(0,0,0));
		put(Direction.WEST, new Vector3i(1,0,0));
		put(Direction.NORTH_WEST, new Vector3i(1,-1,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-1,0));
	}}), 
	RUIN_3("Ruin3", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(0,0,0));
		put(Direction.WEST, new Vector3i(2,0,0));
		put(Direction.NORTH_WEST, new Vector3i(2,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-2,0));
	}}), 
	TREE_1_10("Tree1_10"),
	TREE_1_11("Tree1_11"),
	TREE_1_12("Tree1_12"),
	TREE_4("Tree4", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,0,0));
		put(Direction.WEST, new Vector3i(2,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(1,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-1,0));
	}}),
	STONE_WALL_4("StoneWall4", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(0,0,0));
		put(Direction.WEST, new Vector3i(3,0,0));
		put(Direction.NORTH_WEST, new Vector3i(3,-3,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-3,0));
	}}),
	CASTLE_BASE_CORNER("CastleBaseCorner"),
	CASTLE_BASE_STRAIGHT("CastleBaseStraight"),
	CASTLE_BASE_END("CastleBaseEnd"),
	CASTLE_WALL_CORNER("CastleWallCorner"),
	CASTLE_WALL_STRAIGHT("CastleWallStraight"),
	CASTLE_WALL_END("CastleWallEnd"),
	CASTLE_DOOR("CastleDoor", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(0,0,0));
		put(Direction.WEST, new Vector3i(2,0,0));
		put(Direction.NORTH_WEST, new Vector3i(2,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-2,0));
	}}),
	CASTLE_ARCH("CastleArch", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(0,0,0));
		put(Direction.WEST, new Vector3i(2,0,0));
		put(Direction.NORTH_WEST, new Vector3i(2,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-2,0));
	}}),
	CASTLE_FLAG("CastleFlag"),
	CASTLE_LADDER("CastleLadder"),
	CASTLE_BATTLEMENT("CastleBattlement"),
	HIVE_6("Hive6", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(1,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,-1,0));
		put(Direction.WEST, new Vector3i(2,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(2,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-2,0));
	}}),
	GLACIER_1("Glacier1"),
	GLACIER_3("Glacier3", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,0,0));
		put(Direction.WEST, new Vector3i(1,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(1,-1,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-1,0));
	}}),
	GLACIER_4("Glacier4", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,0,0));
		put(Direction.WEST, new Vector3i(2,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(1,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-1,0));
	}}),
	GLACIER_6("Glacier6", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(1,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,-1,0));
		put(Direction.WEST, new Vector3i(2,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(2,-2,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-2,0));
	}}),
	PALM_1("Palm1"),
	BRUSH_1("Brush1"),
	OUTCROP_1("Outcrop1"),
	OUTCROP_3("Outcrop3", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,0,0));
		put(Direction.WEST, new Vector3i(1,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(1,-1,0));
		put(Direction.NORTH_EAST, new Vector3i(0,-1,0));
	}}),
	RUIN_6("Ruin6", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,0,0));
		put(Direction.WEST, new Vector3i(4,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(3,-4,0));
		put(Direction.NORTH_EAST, new Vector3i(-1,-3,0));
	}}),
	RUIN_6_BREAKED("Ruin6Breaked", new HashMap<Direction, Vector3i>() {{
		put(Direction.EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
		put(Direction.SOUTH_WEST, new Vector3i(1,0,0));
		put(Direction.WEST, new Vector3i(4,-1,0));
		put(Direction.NORTH_WEST, new Vector3i(3,-4,0));
		put(Direction.NORTH_EAST, new Vector3i(-1,-3,0));
	}})
	;
	
	private String externalModelName;
	private Map<Direction, Vector3i> attachementTilePosition;
	
	private VirtualScapeDecorType(String externalModelName, Map<Direction, Vector3i> attachementTilePosition) {
		this.externalModelName = externalModelName;
		this.attachementTilePosition = attachementTilePosition;
	}
	
	private VirtualScapeDecorType(String externalModelName) {
		this.externalModelName = externalModelName;
		this.attachementTilePosition = new HashMap<Direction, Vector3i>() {{
			put(Direction.EAST, new Vector3i(0,0,0));
			put(Direction.SOUTH_EAST, new Vector3i(0,0,0));
			put(Direction.SOUTH_WEST, new Vector3i(0,0,0));
			put(Direction.WEST, new Vector3i(0,0,0));
			put(Direction.NORTH_WEST, new Vector3i(0,0,0));
			put(Direction.NORTH_EAST, new Vector3i(0,0,0));
		}};
	}


	public String getExternalModelName() {
		return externalModelName;
	}


	public Map<Direction, Vector3i> getAttachementTilePosition() {
		return attachementTilePosition;
	}
	
	
}
