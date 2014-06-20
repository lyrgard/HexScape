package fr.lyrgard.hexScape.service;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

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
		}
		if (direction != piece.getDirection()) {
			rotate(direction);
		}
	}
}
