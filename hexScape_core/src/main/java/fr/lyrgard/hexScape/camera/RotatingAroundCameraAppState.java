package fr.lyrgard.hexScape.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class RotatingAroundCameraAppState extends AbstractAppState {

	private RotatingAroundCamera rotatingAroundCamera;
	
	private Spatial rotateAroundNode;
	
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		if (app.getInputManager() != null){
			if (this.rotatingAroundCamera == null) {
				this.rotatingAroundCamera = new RotatingAroundCamera(app.getCamera());
			}
			this.rotatingAroundCamera.registerWithInput(app.getInputManager());
			if (rotateAroundNode != null) {
				rotatingAroundCamera.setRotateAroundNode(rotateAroundNode);
			}
		}
	}
	
	public void cleanup() {
		super.cleanup();
		rotatingAroundCamera.unregisterInput();
	}
	
	public void setRotateAroundNode(Spatial rotateAroundNode) {
		this.rotateAroundNode = rotateAroundNode;
		if (rotatingAroundCamera != null) {
			rotatingAroundCamera.setRotateAroundNode(rotateAroundNode);
		} 
	}
}
