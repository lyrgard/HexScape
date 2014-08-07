package fr.lyrgard.hexScape.control;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.model.TitleScreen;
import fr.lyrgard.hexScape.model.TitleScreenSprite;
import fr.lyrgard.hexScape.model.TitleScreenButtonClicked;

public class TitleMenuButtonsAppState extends AbstractAppState implements ActionListener {
	private static final String CLICK_MAPPING = "TitleMenuButtonssAppState_click";

	private InputManager inputManager;
	
	private Camera cam;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		
		inputManager = app.getInputManager();
		cam = app.getCamera();
		
		
		inputManager.addMapping(CLICK_MAPPING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	
		inputManager.addListener(this, CLICK_MAPPING);
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		if (!isEnabled()) {
			return;
		}
		
		if (name.equals(CLICK_MAPPING) && !keyPressed) {
			Geometry geometry = getButtonCollisionPoint();
			if (geometry != null && geometry instanceof TitleScreenSprite) {
				GuiMessageBus.post(new TitleScreenButtonClicked(((TitleScreenSprite)geometry).getType()));
			}
		}
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		Geometry geometry = getButtonCollisionPoint();
			
		for (Spatial spatial : TitleScreen.getInstance().getButtons().getChildren()) {
			if (spatial instanceof TitleScreenSprite) {
				TitleScreenSprite button = (TitleScreenSprite)spatial;
				if (button == geometry) {
					button.selected();
				} else {
					button.notSelected();
				}
			}
		}
	}
	
	private Geometry getButtonCollisionPoint() {
		Geometry target = null;
		
		// Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        // Collect intersections between ray and all nodes in results list.
        Spatial buttonNode = TitleScreen.getInstance().getButtons();
        if (buttonNode != null) {
        	buttonNode.collideWith(ray, results);
        }
		// 5. Use the results 
		if (results.size() > 0) {
			// The closest result is the target that the player picked:
			target = results.getClosestCollision().getGeometry();
//			collision = results.getClosestCollision().getContactPoint();
//			
//			CoordinateUtils.centerPosOnHex(collision);
			
		} 
		
		return target;
	}
}
