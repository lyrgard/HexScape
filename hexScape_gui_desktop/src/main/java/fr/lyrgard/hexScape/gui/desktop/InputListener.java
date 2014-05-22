package fr.lyrgard.hexScape.gui.desktop;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import com.jme3.input.RawInputListener;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

public class InputListener implements RawInputListener {

	private JFrame frame;
	
	public InputListener(JFrame frame) {
		this.frame = frame;
	}
	
	public void beginInput() {
		// TODO Auto-generated method stub
		
	}

	public void endInput() {
		// TODO Auto-generated method stub
		
	}

	public void onJoyAxisEvent(JoyAxisEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onJoyButtonEvent(JoyButtonEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyEvent(KeyInputEvent event) {
		
		
		Component component = frame.getFocusOwner();
		
		if (component != null) {
			if (event.isPressed()) {
				KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED,  EventQueue.getMostRecentEventTime(), 0, AwtKeyInput.convertJmeCode(event.getKeyCode()), event.getKeyChar());
				System.out.println("Key pressed : " + AwtKeyInput.convertJmeCode(event.getKeyCode()) + ", " + event.getKeyChar());
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(keyEvent);
				
				
				try {
					keyEvent = new KeyEvent(component, KeyEvent.KEY_TYPED,  EventQueue.getMostRecentEventTime(), 0, KeyEvent.VK_UNDEFINED, event.getKeyChar());
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(keyEvent);
					System.out.println("Key typed : " + AwtKeyInput.convertJmeCode(event.getKeyCode()) + ", " + event.getKeyChar());
				} catch (IllegalArgumentException e) {
					
				}
			} else {
				KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_RELEASED,  EventQueue.getMostRecentEventTime(), 0, AwtKeyInput.convertJmeCode(event.getKeyCode()), event.getKeyChar());
				System.out.println("Key released : " + AwtKeyInput.convertJmeCode(event.getKeyCode()) + ", " + event.getKeyChar());
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(keyEvent);
			}			
		}
		
		
	}

	public void onMouseButtonEvent(MouseButtonEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseMotionEvent(MouseMotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onTouchEvent(TouchEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
