package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.map.Direction;

public class PiecePlacedMessage extends AbstractPieceMessage {

	private String modelId;
	private String cardInstanceId;
	
	private int x;
	private int y;
	private int z;
	private Direction direction;
	
	@JsonCreator
	public PiecePlacedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("cardInstanceId") String cardInstanceId, 
			@JsonProperty("pieceId") String pieceId, 
			@JsonProperty("modelId") String modelId,
			@JsonProperty("x") int x,
			@JsonProperty("y") int y,
			@JsonProperty("z") int z,
			@JsonProperty("direction") Direction direction) {
		super(playerId, pieceId);
		this.modelId = modelId;
		this.cardInstanceId = cardInstanceId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}

	public String getModelId() {
		return modelId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getCardInstanceId() {
		return cardInstanceId;
	}
	
}
