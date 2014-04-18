package fr.lyrgard.hexScape.model;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public enum Direction {
	TOP(0, new Vector3f(0, 1, 0)), 
	BOTTOM(0, new Vector3f(0, -1, 0)), 
	NORTH_EAST(60 * FastMath.DEG_TO_RAD, new Vector3f(0.5f, 0, FastMath.sin(60 * FastMath.DEG_TO_RAD))), 
	EAST(0, new Vector3f(1, 0, 0)), 
	SOUTH_EAST(300 * FastMath.DEG_TO_RAD, new Vector3f(0.5f, 0, -FastMath.sin(60 * FastMath.DEG_TO_RAD))), 
	SOUTH_WEST(240 * FastMath.DEG_TO_RAD, new Vector3f(-0.5f, 0, -FastMath.sin(60 * FastMath.DEG_TO_RAD))), 
	WEST(FastMath.PI, new Vector3f(-1, 0, 0)), 
	NORTH_WEST(120 * FastMath.DEG_TO_RAD, new Vector3f(-0.5f, 0, FastMath.sin(60 * FastMath.DEG_TO_RAD)));
	
	private float angle;
	private Vector3f normal;
	
	
	private Direction(float angle, Vector3f normal) {
		this.angle = angle;
		this.normal = normal;
	}



	public Direction getOpposite() {
		switch (this) {
		case TOP:
			return BOTTOM;
		case BOTTOM:
			return TOP;
		case NORTH_EAST:
			return SOUTH_WEST;
		case EAST:
			return WEST;
		case SOUTH_EAST:
			return NORTH_WEST;
		case SOUTH_WEST:
			return NORTH_EAST;
		case WEST:
			return EAST;
		case NORTH_WEST:
			return SOUTH_EAST;

		}
		return null;
	}



	public float getAngle() {
		return angle;
	}



	public Vector3f getNormal() {
		return normal;
	}
}
