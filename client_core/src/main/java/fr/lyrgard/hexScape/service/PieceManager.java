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

	public PieceManager(PieceInstance piece) {
		super();
		this.piece = piece;
	}



	public PieceInstance getPiece() {
		return piece;
	}
	
	public Spatial getSpatial() {
		if (pieceNode == null) {
			pieceNode = new Node();
			pieceNode.attachChild(ExternalModelService.getInstance().getModel(piece.getModelId()));
		}
		return pieceNode;
	}
	
	public void rotate(Direction direction) {
		getPiece().setDirection(direction);
		
		float angle = DirectionService.getInstance().getAngle(direction);
		getSpatial().setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
	}
	
	public void moveTo(int x, int y, int z, Direction direction) {
		Tile nearestTile = HexScapeCore.getInstance().getMapManager().getNearestTile(x, y, z);
		if (nearestTile != null) {
			Vector3f spacePos = CoordinateUtils.toSpaceCoordinate(nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());

			if (TileService.getInstance().isHalfTile(nearestTile.getType())) {
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
		pieceNode.attachChild(selectMarker.getSpatial());
		selectMarker.getSpatial().setLocalTranslation(0, 0.3f, 0);
	}
	
	public void unselect(String playerId) {
		pieceNode.detachChild(SelectMarkerService.getInstance().getSelectMarker(playerId).getSpatial());
	}
}
