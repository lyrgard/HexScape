package fr.lyrgard.hexScape.control;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.HexScapeJme3Application;
import fr.lyrgard.hexScape.io.virtualScape.bean.Vector3i;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.service.PieceManager;
import fr.lyrgard.hexScape.service.TileService;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class PlacePieceByMouseAppState extends AbstractAppState {
	
	private PieceManager pieceToPlace;
	
	private InputManager inputManager;
	
	private Camera cam;
	
	private Node rootNode;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.inputManager = app.getInputManager();
		this.cam = app.getCamera();
		this.rootNode = ((HexScapeJme3Application)app).getRootNode();
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

	public PieceManager getPieceToPlace() {
		return pieceToPlace;
	}

	public void setPieceToPlace(PieceManager newPieceToPlace) {
		if (this.pieceToPlace != null) {
			rootNode.detachChild(this.pieceToPlace.getSpatial());
			if (HexScapeCore.getInstance().getHexScapeJme3Application().getScene().contains(pieceToPlace)) {
				HexScapeCore.getInstance().getMapManager().placePiece(pieceToPlace, pieceToPlace.getPiece().getX(), pieceToPlace.getPiece().getY(), pieceToPlace.getPiece().getZ());
			}
		}
		
		this.pieceToPlace = newPieceToPlace;
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		Vector3f collision = getMapCollisionPoint();
			
		if (collision != null) {
			if (HexScapeCore.getInstance().getMapManager() != null) {
				Vector3i mapPos = CoordinateUtils.toMapCoordinate(collision.x, collision.y, collision.z);
				Tile nearestTile = HexScapeCore.getInstance().getMapManager().getNearestTile(mapPos.x, mapPos.y, mapPos.z);
				if (nearestTile != null) {
					Vector3f spacePos = CoordinateUtils.toSpaceCoordinate(nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());

					if (TileService.getInstance().isHalfTile(nearestTile.getType())) {
						spacePos.y += TileMesh.HEX_SIZE_Y / 2;
					} else {
						spacePos.y += TileMesh.HEX_SIZE_Y;
					}
					pieceToPlace.getSpatial().setLocalTranslation(spacePos);
				}
			}
		}
	}
	
	public boolean placePiece() {
		boolean success = false;
		Vector3f collision = getMapCollisionPoint();
		if (collision != null) {
			Vector3i mapPos = CoordinateUtils.toMapCoordinate(collision.x, collision.y, collision.z);
			
			success = HexScapeCore.getInstance().getMapManager().placePiece(pieceToPlace, mapPos.x, mapPos.y, mapPos.z);
			setPieceToPlace(null);
		}
		return success;
	}
	
	public void cancelPlacement() {
		setPieceToPlace(null);
	}

	private Vector3f getMapCollisionPoint() {
		Vector3f collision = null;
		
		// Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        // Collect intersections between ray and all nodes in results list.
        Spatial mapSpatial = HexScapeCore.getInstance().getMapManager().getMapWithoutDecorsSpatial();
        if (mapSpatial != null) {
        	mapSpatial.collideWith(ray, results);
        }
		// 5. Use the results 
		if (results.size() > 0) {
			// The closest result is the target that the player picked:
			//Geometry target = results.getClosestCollision().getGeometry();
			collision = results.getClosestCollision().getContactPoint();
			
			CoordinateUtils.centerPosOnHex(collision);
			
		} 
		
		return collision;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		if (enabled) {
			if (pieceToPlace != null) {
				rootNode.attachChild(pieceToPlace.getSpatial());
			}
		} else {
			setPieceToPlace(null);
		}
	}
}
