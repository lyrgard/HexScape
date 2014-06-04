package fr.lyrgard.hexScape.service;

import com.jme3.scene.Spatial;

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
}
