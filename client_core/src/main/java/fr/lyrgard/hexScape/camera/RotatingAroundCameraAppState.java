package fr.lyrgard.hexScape.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;


public class RotatingAroundCameraAppState extends AbstractAppState {

	private RotatingAroundCamera rotatingAroundCamera;
	
	private Application app;
	
	private Spatial rotateAroundNode;
	
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
		if (app.getInputManager() != null){
			if (this.rotatingAroundCamera == null) {
				this.rotatingAroundCamera = new RotatingAroundCamera(app.getCamera());
			}
			if (isEnabled()) {
				this.rotatingAroundCamera.registerWithInput(app.getInputManager());
			}
			if (rotateAroundNode != null) {
				rotatingAroundCamera.setRotateAroundNode(rotateAroundNode, true);
			}
		}
	}
	
	public void cleanup() {
		super.cleanup();
		rotatingAroundCamera.unregisterInput();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (rotatingAroundCamera != null) {
			if (enabled) {
				this.rotatingAroundCamera.registerWithInput(app.getInputManager());
			} else {
				this.rotatingAroundCamera.unregisterInput();
			}
		}
		super.setEnabled(enabled);
	}

	public void setRotateAroundNode(Spatial rotateAroundNode, boolean resetPosition) {
		this.rotateAroundNode = rotateAroundNode;
		if (rotatingAroundCamera != null) {
			rotatingAroundCamera.setRotateAroundNode(rotateAroundNode, resetPosition);
		} 
	}
}
