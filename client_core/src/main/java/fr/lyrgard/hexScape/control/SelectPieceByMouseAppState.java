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
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.io.virtualScape.bean.Vector3i;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.PieceSecondarySelectedMessage;
import fr.lyrgard.hexScape.message.PieceSecondaryUnselectedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.service.PieceManager;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class SelectPieceByMouseAppState extends AbstractAppState {

	
	private PieceManager selectedPiece;
	
	private InputManager inputManager;
	
	private Camera cam;
	
	//private Spatial selectMarker;
	
	//private AmbientLight selectedLigth;
	
	//private float selectMarkerY;
	
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
	
	public boolean secondarySelectPiece() {
		boolean success = false;
		PieceManager pieceToSencondarySelect = getPieceUnderMouse();
		if (pieceToSencondarySelect != null && selectedPiece != pieceToSencondarySelect) {
			pieceToSencondarySelect.switchSecondarySelect(CurrentUserInfo.getInstance().getPlayerId(), selectedPiece);
			AbstractMessage message;
			if (pieceToSencondarySelect.isSecondarySelected()) {
				message = new PieceSecondarySelectedMessage(CurrentUserInfo.getInstance().getPlayerId(), pieceToSencondarySelect.getPiece().getId(), selectedPiece.getPiece().getId());
			} else {
				message = new PieceSecondaryUnselectedMessage(CurrentUserInfo.getInstance().getPlayerId(), pieceToSencondarySelect.getPiece().getId(), selectedPiece.getPiece().getId());
			}
			CoreMessageBus.post(message);
			success = true;
		}
		return success;
	}
	
	public void selectPiece(PieceManager piece) {
		if (piece != null) {
			piece.select(CurrentUserInfo.getInstance().getPlayerId());
			selectedPiece = piece;
		}
	}
	
	public void cancelSelection() {
		if (selectedPiece != null) {
			selectedPiece.unselect(CurrentUserInfo.getInstance().getPlayerId());
			selectedPiece = null;
		}
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
	
}
