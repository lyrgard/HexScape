package fr.lyrgard.hexScape.service;

import java.util.HashMap;
import java.util.Map;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.model.map.Direction;

public class DirectionService {

	private static DirectionService INSTANCE;

	public static synchronized DirectionService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DirectionService();
		}
		return INSTANCE;
	}

	private Map<Direction, Float> angles = new HashMap<>();

	private Map<Direction, Vector3f> normals = new HashMap<>();

	private DirectionService() {
		angles.put(Direction.TOP,0f); 
		angles.put(Direction.BOTTOM,0f);
		angles.put(Direction.NORTH_EAST,60 * FastMath.DEG_TO_RAD); 
		angles.put(Direction.EAST,0f); 
		angles.put(Direction.SOUTH_EAST,300 * FastMath.DEG_TO_RAD); 
		angles.put(Direction.SOUTH_WEST,240 * FastMath.DEG_TO_RAD); 
		angles.put(Direction.WEST,FastMath.PI); 
		angles.put(Direction.NORTH_WEST,120 * FastMath.DEG_TO_RAD);

		normals.put(Direction.TOP, new Vector3f(0, 1, 0)); 
		normals.put(Direction.BOTTOM, new Vector3f(0, -1, 0)); 
		normals.put(Direction.NORTH_EAST, new Vector3f(0.5f, 0, FastMath.sin(60 * FastMath.DEG_TO_RAD))); 
		normals.put(Direction.EAST, new Vector3f(1, 0, 0)); 
		normals.put(Direction.SOUTH_EAST, new Vector3f(0.5f, 0, -FastMath.sin(60 * FastMath.DEG_TO_RAD))); 
		normals.put(Direction.SOUTH_WEST, new Vector3f(-0.5f, 0, -FastMath.sin(60 * FastMath.DEG_TO_RAD))); 
		normals.put(Direction.WEST, new Vector3f(-1, 0, 0)); 
		normals.put(Direction.NORTH_WEST, new Vector3f(-0.5f, 0, FastMath.sin(60 * FastMath.DEG_TO_RAD)));
	}


	public float getAngle(Direction direction) {
		return angles.get(direction);
	}
	
	public Vector3f getNormal(Direction direction) {
		return normals.get(direction);
	}
	
	public Direction rotate(Direction direction, boolean clockwise) {
		if (clockwise) {
			switch (direction) {
			case TOP:
				return null;
			case BOTTOM:
				return null;
			case NORTH_EAST:
				return Direction.EAST;
			case EAST:
				return Direction.SOUTH_EAST;
			case SOUTH_EAST:
				return Direction.SOUTH_WEST;
			case SOUTH_WEST:
				return Direction.WEST;
			case WEST:
				return Direction.NORTH_WEST;
			case NORTH_WEST:
				return Direction.NORTH_EAST;
			}
		} else {
			switch (direction) {
			case TOP:
				return null;
			case BOTTOM:
				return null;
			case NORTH_EAST:
				return Direction.NORTH_WEST;
			case EAST:
				return Direction.NORTH_EAST;
			case SOUTH_EAST:
				return Direction.EAST;
			case SOUTH_WEST:
				return Direction.SOUTH_EAST;
			case WEST:
				return Direction.SOUTH_WEST;
			case NORTH_WEST:
				return Direction.WEST;
			}
		}
		return null;
	}
	

	public Direction getOpposite(Direction direction) {
		switch (direction) {
		case TOP:
			return Direction.BOTTOM;
		case BOTTOM:
			return Direction.TOP;
		case NORTH_EAST:
			return Direction.SOUTH_WEST;
		case EAST:
			return Direction.WEST;
		case SOUTH_EAST:
			return Direction.NORTH_WEST;
		case SOUTH_WEST:
			return Direction.NORTH_EAST;
		case WEST:
			return Direction.EAST;
		case NORTH_WEST:
			return Direction.SOUTH_EAST;

		}
		return null;
	}
}
