package fr.lyrgard.hexScape.model.piece;

import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.map.Direction;

public class PieceInstance {
	
	private String id;
	
	private CardInstance card;

	private int x;
	
	private int y;
	
	private int z;
	
	private Direction direction = Direction.EAST;
	
	private String modelId;
	
	public PieceInstance(String id, String modelId, CardInstance card) {
		this.id = id;
		this.modelId= modelId; 
		this.card = card;
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

	public String getModelId() {
		return modelId;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CardInstance getCard() {
		return card;
	}
}
