package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.map.Direction;

public class PieceMovedMessage extends AbstractPieceMessage {

	private int x;
	private int y;
	private int z;
	private Direction direction;
	
	@JsonCreator
	public PieceMovedMessage(
			@JsonProperty("playerId") String playerId, 
			@JsonProperty("pieceId") String pieceId, 
			@JsonProperty("x") int x,
			@JsonProperty("y") int y,
			@JsonProperty("z") int z,
			@JsonProperty("direction") Direction direction) {
		super(playerId, pieceId);
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
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
}
