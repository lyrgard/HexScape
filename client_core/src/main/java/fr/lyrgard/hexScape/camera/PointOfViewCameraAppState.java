package fr.lyrgard.hexScape.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.service.PieceManager;

public class PointOfViewCameraAppState extends AbstractAppState {

	private PointOfViewCamera pointOfViewCamera;
	
	private Application app;
	
	PieceManager piece;
	
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
		if (app.getInputManager() != null){
			if (this.pointOfViewCamera == null) {
				this.pointOfViewCamera = new PointOfViewCamera(app.getCamera());
			}
			if (isEnabled()) {
				this.pointOfViewCamera.registerWithInput(app.getInputManager());
			}
		}
	}
	
	public void cleanup() {
		super.cleanup();
		pointOfViewCamera.unregisterFromInput();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (pointOfViewCamera != null) {
			if (enabled) {
				this.pointOfViewCamera.registerWithInput(app.getInputManager());
			} else {
				this.pointOfViewCamera.unregisterFromInput();
			}
		}
		super.setEnabled(enabled);
	}

	public void setPiece(PieceManager piece) {
		this.piece = piece;
		if (pointOfViewCamera != null) {
			BoundingBox bv = (BoundingBox)piece.getSpatial().getWorldBound();
			Vector3f pos = bv.getCenter();
			pos.y = pos.y + bv.getYExtent()*2f/5f;
			
			
			pointOfViewCamera.setPosition(pos, piece.getPiece().getDirection());
		} 
	}
}
