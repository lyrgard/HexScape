package fr.lyrgard.hexScape.control;

import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.io.virtualScape.bean.Vector3i;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.SelectMarker;
import fr.lyrgard.hexScape.service.PieceManager;
import fr.lyrgard.hexScape.service.SelectMarkerService;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class SelectPieceByMouseAppState extends AbstractAppState {

	
	private PieceManager selectedPiece;
	
	private InputManager inputManager;
	
	private Camera cam;
	
	//private Spatial selectMarker;
	
	//private AmbientLight selectedLigth;
	
	//private float selectMarkerY;
	private float selectMarkerYOffset = 0.3f;
	private float selectMarkerYVariation = 0.2f;
	private float time = 0;
	
	private float selectMarkerRotation;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.inputManager = app.getInputManager();
		this.cam = app.getCamera();
		
		//selectMarker = new SelectMarker().getSpatial();
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}
	

	public boolean selectPiece() {
		boolean success = false;
		PieceManager piece = getPieceUnderMouse();
		if (piece != null && selectedPiece != piece) {
			selectPiece(piece);
			success = true;
		}
		return success;
	}
	
	public void selectPiece(PieceManager piece) {
		if (piece != null) {
			cancelSelection();
			selectedPiece = piece;

			piece.select(CurrentUserInfo.getInstance().getPlayerId());

//			BoundingBox boundingBox = (BoundingBox)selectedPiece.getSpatial().getWorldBound();
//
//			Vector3f spacePos = CoordinateUtils.toSpaceCoordinate(piece.getPiece().getX(), piece.getPiece().getY(), piece.getPiece().getZ());
//			selectMarkerY = boundingBox.getCenter().y - boundingBox.getYExtent() + selectMarkerYOffset;
//			spacePos.y = selectMarkerY;
//			selectMarker.setLocalTranslation(spacePos);
//
//			rootNode.attachChild(selectMarker);
		}
	}
	
	public void cancelSelection() {
		if (selectedPiece != null) {
			//selectedPiece.getSpatial().removeLight(selectedLigth);
			selectedPiece.unselect(CurrentUserInfo.getInstance().getPlayerId());
		}
		//rootNode.detachChild(selectMarker);
		selectedPiece = null;
	}



	public PieceManager getPieceUnderMouse() {
		PieceManager piece = null;
		
		// Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        // Collect intersections between ray and all nodes in results list.
        Spatial selectablePieces = HexScapeCore.getInstance().getHexScapeJme3Application().getScene().getSelectablePieceNode();
        if (selectablePieces != null) {
        	selectablePieces.collideWith(ray, results);
        }
		// 5. Use the results 
		if (results.size() > 0) {
			// The closest result is the target that the player picked:
			//Geometry target = results.getClosestCollision().getGeometry();
			Vector3f collision = results.getClosestCollision().getContactPoint();
			
			Vector3i mapPos = CoordinateUtils.toMapCoordinate(collision.x, collision.y, collision.z);
			
			piece = HexScapeCore.getInstance().getHexScapeJme3Application().getScene().getNearestPiece(mapPos.x, mapPos.y, mapPos.z);
		} 
		
		return piece;
	}

	public PieceManager getSelectedPiece() {
		return selectedPiece;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		if (enabled) {
			
		} else {
			cancelSelection();
		}
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		time = (time + 2 * tpf) % (Float.MAX_VALUE);
		Collection<SelectMarker> selectMarkers = SelectMarkerService.getInstance().getSelectMarkers();
		int i = 0;
		int number = selectMarkers.size();
		for (SelectMarker selectMarker : selectMarkers) {
			float thisMarkerTime = (time + FastMath.TWO_PI * i/number) % FastMath.TWO_PI;
			selectMarkerRotation = (thisMarkerTime + tpf/128) % FastMath.TWO_PI;
			Vector3f localTranslation = selectMarker.getSpatial().getLocalTranslation();
			localTranslation.y = selectMarkerYOffset + selectMarkerYVariation * FastMath.sin(thisMarkerTime);
			selectMarker.getSpatial().setLocalTranslation(localTranslation);
			selectMarker.getSpatial().getLocalRotation().fromAngleAxis(selectMarkerRotation, Vector3f.UNIT_Y);
			i++;
		}
	}
	
}
