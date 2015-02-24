package fr.lyrgard.hexScape.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.service.PieceManager;

public class PointOfViewCameraAppState extends AbstractAppState {

	private PointOfViewCamera pointOfViewCamera;
	
	private Application app;
	
	private Vector3f savedCameraPosition;
	
	private Quaternion savedCameraRotation;
	
	
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
				savedCameraPosition = app.getCamera().getLocation().clone();
				savedCameraRotation = app.getCamera().getRotation().clone();
				this.pointOfViewCamera.registerWithInput(app.getInputManager());
			} else {
				this.pointOfViewCamera.unregisterFromInput();
				if (this.isEnabled() && savedCameraPosition != null && savedCameraRotation != null) {
					app.getCamera().setLocation(savedCameraPosition);
					app.getCamera().setRotation(savedCameraRotation);
				}
			}
		}
		super.setEnabled(enabled);
	}

	public void setPiece(PieceManager piece) {
		if (pointOfViewCamera != null) {
			BoundingBox bv = (BoundingBox)piece.getSpatial().getWorldBound();
			Vector3f pos = bv.getCenter();
			pos.y = pos.y + bv.getYExtent()*9f/10f;
			
			
			pointOfViewCamera.setPosition(pos, piece.getPiece().getDirection());
		} 
	}
}
