package fr.lyrgard.hexScape.service;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.SelectMarker;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class PieceManager {

	private PieceInstance piece;
	
	private Node pieceNode;
	
	private Spatial pieceModelSpatial;
	
	private boolean selected = false;
	
	private boolean secondarySelected = false;

	public PieceManager(PieceInstance piece) {
		super();
		this.piece = piece;
	}



	public PieceInstance getPiece() {
		return piece;
	}
	
	public Node getSpatial() {
		if (pieceNode == null) {
			pieceNode = new Node();
			pieceModelSpatial = ExternalModelService.getInstance().getModel(piece.getModelId());
			pieceNode.attachChild(pieceModelSpatial);
			rotate(getPiece().getDirection());
		}
		return pieceNode;
	}
	
	public void rotate(Direction direction) {
		getPiece().setDirection(direction);
		
		if (pieceModelSpatial != null) {
			float angle = DirectionService.getInstance().getAngle(direction);
			pieceModelSpatial.setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
		}
	}
	
	public void moveTo(int x, int y, int z, Direction direction) {
		Tile nearestTile = HexScapeCore.getInstance().getMapManager().getNearestTile(x, y, z);
		if (nearestTile != null) {
			Vector3f spacePos = CoordinateUtils.toSpaceCoordinate(nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());

			if (nearestTile.isHalfSize()) {
				spacePos.y += TileMesh.HEX_SIZE_Y / 2;
			} else {
				spacePos.y += TileMesh.HEX_SIZE_Y;
			}
			getSpatial().setLocalTranslation(spacePos);
			piece.setX(x);
			piece.setY(y);
			piece.setZ(z);
		}
		if (direction != piece.getDirection()) {
			rotate(direction);
		}
	}
	
	public void select(String playerId) {
		SelectMarker selectMarker = SelectMarkerService.getInstance().getSelectMarker(playerId);
		selectMarker.detach();
		selectMarker.attachTo(this);
		selected = true;
	}
	
	public void unselect(String playerId) {
		SelectMarker selectMarker = SelectMarkerService.getInstance().getSelectMarker(playerId);
		selectMarker.detach();
		selected = false;
	}

	public void switchSecondarySelect(String playerId, PieceManager selectedPiece) {
		SelectMarker selectMarker = SelectMarkerService.getInstance().getSelectMarker(playerId);
		secondarySelected = selectMarker.switchSecondarySelect(this);
	}


	public boolean isSelected() {
		return selected;
	}
	
	public boolean isSecondarySelected() {
		return secondarySelected;
	}
	
	
}
