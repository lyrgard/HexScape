package fr.lyrgard.hexScape.gui.desktop.technical;

import java.awt.Component;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.system.AppSettings;
import com.jme3.system.lwjgl.LwjglCanvas;




/**
 * Jme 3 Context to change the inputs to AwtInput so work well with Swing
 * @author krnw5701
 *
 */
public class SwingContext extends LwjglCanvas {

	private AwtKeyInput keyInput = new AwtKeyInput();
	private AwtMouseInput mouseInput = new AwtMouseInput();
	
	@Override
	public KeyInput getKeyInput() {
		return keyInput;
	}
	

	@Override
	public MouseInput getMouseInput() {
		return mouseInput;
	}

	

	public void setInputSource(Component comp) {
		keyInput.setInputSource(comp);
		mouseInput.setInputSource(comp);
	}
	
	public void setSettings(AppSettings settings) {
        this.settings.copyFrom(settings);
        this.settings.setRenderer(AppSettings.LWJGL_OPENGL2);
    }



}
