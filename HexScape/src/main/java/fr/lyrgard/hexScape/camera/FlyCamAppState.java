package fr.lyrgard.hexScape.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

public class FlyCamAppState extends com.jme3.app.FlyCamAppState {

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		app.getInputManager().deleteMapping("FLYCAM_Forward");
		app.getInputManager().deleteMapping("FLYCAM_Lower");
		app.getInputManager().deleteMapping("FLYCAM_StrafeLeft");
		app.getInputManager().deleteMapping("FLYCAM_Rise");
		app.getInputManager().addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_Z));
		app.getInputManager().addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_W));
		app.getInputManager().addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_Q));
		app.getInputManager().addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_A));
		app.getInputManager().addListener(getCamera(), new String[] {"FLYCAM_Forward", "FLYCAM_Lower", "FLYCAM_StrafeLeft", "FLYCAM_Rise"});
		getCamera().setMoveSpeed(10f);
	}

}
