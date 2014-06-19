package fr.lyrgard.hexScape.service;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.piece.PieceInstance;

public class PieceManager {

	private PieceInstance piece;
	
	private Spatial pieceNode;

	public PieceManager(PieceInstance piece) {
		super();
		this.piece = piece;
	}



	public PieceInstance getPiece() {
		return piece;
	}
	
	public Spatial getSpatial() {
		if (pieceNode == null) {
			pieceNode = ExternalModelService.getInstance().getModel(piece.getModelId());
		}
		return pieceNode;
	}
	
	public void rotate(Direction direction) {
		getPiece().setDirection(direction);
		
		float angle = DirectionService.getInstance().getAngle(direction);
		getSpatial().setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
	}
}
