package fr.lyrgard.hexScape.model;

import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.service.ExternalModelService;

public class MoveablePiece implements Displayable {
	
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private int x;
	
	private int y;
	
	private int z;
	
	private Direction direction = Direction.EAST;
	
	private String modelName;

	private Spatial pieceNode;
	
	public MoveablePiece(String modelName) {
		this.modelName= modelName; 
	}
	
	@Override
	public Spatial getSpatial() {
		if (pieceNode == null) {
			pieceNode = ExternalModelService.getInstance().getModel(modelName);
		}
		return pieceNode;
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

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getModelName() {
		return modelName;
	}

}
