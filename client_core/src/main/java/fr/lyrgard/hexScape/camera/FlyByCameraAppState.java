package fr.lyrgard.hexScape.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class FlyByCameraAppState extends AbstractAppState {

		private FlyByCamera flyByCamera;

		private Application app;

		public void initialize(AppStateManager stateManager, Application app) {
			super.initialize(stateManager, app);
			this.app = app;
			if (app.getInputManager() != null){
				if (this.flyByCamera == null) {
					this.flyByCamera = new FlyByCamera(app.getCamera());
				}
				if (isEnabled()) {
					this.flyByCamera.registerWithInput(app.getInputManager());
				}
			}
		}

		public void cleanup() {
			super.cleanup();
			flyByCamera.unregisterInput();
		}

		@Override
		public void setEnabled(boolean enabled) {
			if (flyByCamera != null) {
				if (enabled) {
					this.flyByCamera.registerWithInput(app.getInputManager());
				} else {
					this.flyByCamera.unregisterInput();
				}
			}
			super.setEnabled(enabled);
		}
	}
