package fr.lyrgard.hexScape.control;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.WorldGetFocusMessage;

public class FocusControllerAppState extends AbstractAppState  implements ActionListener {
	public static final String FOCUS_LISTENNER_MOUSE_RIGHT_CLICK = "focusListenner_MouseRightClick";
	public static final String FOCUS_LISTENNER_MOUSE_LEFT_CLICK = "focusListenner_MouseLeftClick";

	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		String[] mappings = new String[]{
				FOCUS_LISTENNER_MOUSE_LEFT_CLICK,
				FOCUS_LISTENNER_MOUSE_RIGHT_CLICK
		};
		
		app.getInputManager().addMapping(FOCUS_LISTENNER_MOUSE_RIGHT_CLICK, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		app.getInputManager().addMapping(FOCUS_LISTENNER_MOUSE_LEFT_CLICK, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		
		app.getInputManager().addListener(this, mappings);
	}
	
	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (value) {
			if (name.equals(FOCUS_LISTENNER_MOUSE_RIGHT_CLICK) || name.equals(FOCUS_LISTENNER_MOUSE_LEFT_CLICK)){
				GuiMessageBus.post(new WorldGetFocusMessage());
			}
		}
	}

}
