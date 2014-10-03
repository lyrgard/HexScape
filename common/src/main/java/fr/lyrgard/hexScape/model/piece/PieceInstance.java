package fr.lyrgard.hexScape.model.piece;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.map.Direction;

public class PieceInstance {
	
	private String id;
	
	@JsonIgnore
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
	
	@JsonCreator
	public PieceInstance(
			@JsonProperty("id") String id, 
			@JsonProperty("modelId") String modelId, 
			@JsonProperty("direction") Direction direction, 
			@JsonProperty("x") int x, 
			@JsonProperty("y") int y, 
			@JsonProperty("z") int z) {
		this.id = id;
		this.modelId= modelId; 
		this.direction = direction;
		this.x = x;
		this.y = y;
		this.z = z;
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

	@JsonIgnore
	public CardInstance getCard() {
		return card;
	}

	@JsonIgnore
	public void setCard(CardInstance card) {
		this.card = card;
	}
}
