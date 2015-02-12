package fr.lyrgard.hexScape.camera;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.map.Direction;


public class PointOfViewCamera implements AnalogListener, ActionListener {

	public static final String POVCAM_Left = "POVCAM_Left";
	public static final String POVCAM_Right = "POVCAM_Right";
	public static final String POVCAM_Up = "POVCAM_Up";
	public static final String POVCAM_Down = "POVCAM_Down";
	public static final String POVCAM_Mouse_Left = "POVCAM_Mouse_Left";
	public static final String POVCAM_Mouse_Right = "POVCAM_Mouse_Right";
	public static final String POVCAM_Mouse_Up = "POVCAM_Mouse_Up";
	public static final String POVCAM_Mouse_Down = "POVCAM_Mouse_Down";
	public static final String POVCAM_RotateDrag = "POVCAM_RotateDrag";
	public static final String POVCAM_Exit = "POVCAM_Exit";


	private Camera cam;
	private Vector3f initialUpVec;
	private float rotationSpeed = 1f;
	private boolean dragToRotate = true;
	private boolean canRotate = false;
	private InputManager inputManager;

	/**
	 * Creates a new FlyByCamera to control the given Camera object.
	 * @param cam
	 */
	public PointOfViewCamera(Camera cam){
		this.cam = cam;
		initialUpVec = new Vector3f(0, 1, 0);//cam.getUp().clone();
	}

	/**
	 * Sets the rotation speed.
	 * @param rotationSpeed
	 */
	public void setRotationSpeed(float rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * @return If drag to rotate feature is enabled.
	 *
	 * @see FlyByCamera#setDragToRotate(boolean)
	 */
	public boolean isDragToRotate() {
		return dragToRotate;
	}

	/**
	 * @param dragToRotate When true, the user must hold the mouse button
	 * and drag over the screen to rotate the camera, and the cursor is
	 * visible until dragged. Otherwise, the cursor is invisible at all times
	 * and holding the mouse button is not needed to rotate the camera.
	 * This feature is disabled by default.
	 */
	public void setDragToRotate(boolean dragToRotate) {
		this.dragToRotate = dragToRotate;
		inputManager.setCursorVisible(dragToRotate);
	}

	/**
	 * Registers the FlyByCamera to receive input events from the provided
	 * Dispatcher.
	 * @param dispacher
	 */
	public void registerWithInput(InputManager inputManager){
		this.inputManager = inputManager;

		String[] mappings = new String[]{
				POVCAM_Left,
				POVCAM_Right,
				POVCAM_Up,
				POVCAM_Down,
				POVCAM_Mouse_Left,
				POVCAM_Mouse_Right,
				POVCAM_Mouse_Up,
				POVCAM_Mouse_Down,
				POVCAM_RotateDrag,
				POVCAM_Exit
		};

		// button only - exit
		inputManager.addMapping(POVCAM_Exit, new KeyTrigger(KeyInput.KEY_ESCAPE));
		
		// both mouse and button - rotation of cam
		inputManager.addMapping(POVCAM_Left, new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping(POVCAM_Right, new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping(POVCAM_Up, new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping(POVCAM_Down, new KeyTrigger(KeyInput.KEY_DOWN));
		
		inputManager.addMapping(POVCAM_Mouse_Left, new MouseAxisTrigger(0, true));
		inputManager.addMapping(POVCAM_Mouse_Right, new MouseAxisTrigger(0, false));
		inputManager.addMapping(POVCAM_Mouse_Up, new MouseAxisTrigger(1, false));
		inputManager.addMapping(POVCAM_Mouse_Down, new MouseAxisTrigger(1, true));

		// mouse only - zoom in/out with wheel, and rotate drag
		inputManager.addMapping(POVCAM_RotateDrag, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		inputManager.addListener(this, mappings);
		inputManager.setCursorVisible(dragToRotate);
	}
	
	public void unregisterFromInput(){
		if (this.inputManager == null) {
    		return;
    	}
    	
    	String[] mappings = {POVCAM_Left, POVCAM_Right, POVCAM_Up, POVCAM_Down, POVCAM_Mouse_Left, POVCAM_Mouse_Right, POVCAM_Mouse_Up, POVCAM_Mouse_Down, POVCAM_RotateDrag, POVCAM_Exit};
    	
    	for (String s : mappings) {
    		if (this.inputManager.hasMapping(s)) {
    			this.inputManager.deleteMapping(s);
    		}
    	}
    	this.inputManager.removeListener(this);
	}

	private void rotateCamera(float value, Vector3f axis){


		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(rotationSpeed * value, axis);

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult(up, up);
		mat.mult(left, left);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(left, up, dir);
		q.normalizeLocal();

		cam.setAxes(q);
	}

	public void onAnalog(String name, float value, float tpf) {

		if (name.equals(POVCAM_Left)){
			rotateCamera(value, initialUpVec);
		}else if (name.equals(POVCAM_Right)){
			rotateCamera(-value, initialUpVec);
		}else if (name.equals(POVCAM_Up)){
			rotateCamera(-value, cam.getLeft());
		}else if (name.equals(POVCAM_Down)){
			rotateCamera(value, cam.getLeft());
		} else if (name.equals(POVCAM_Exit)) {
			HexScapeCore.getInstance().getHexScapeJme3Application().lookAtTheMap();
		} else if (canRotate) {
			if (name.equals(POVCAM_Mouse_Left)){
				rotateCamera(value, initialUpVec);
			}else if (name.equals(POVCAM_Mouse_Right)){
				rotateCamera(-value, initialUpVec);
			}else if (name.equals(POVCAM_Mouse_Up)){
				rotateCamera(-value, cam.getLeft());
			}else if (name.equals(POVCAM_Mouse_Down)){
				rotateCamera(value, cam.getLeft());
			}
		}
	}

	public void onAction(String name, boolean value, float tpf) {

		if (name.equals(POVCAM_RotateDrag)){
			canRotate = value;
			inputManager.setCursorVisible(!value);
		}
	}
	
	public void setPosition(Vector3f pos, Direction dir) {
		//cam.setLocation(new Vector3f(0, 100, 0));
		 
		cam.setLocation(pos);
		cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
//		cam.lookAt(new Vector3f(x, y, z), initialUpVec);
	}
}

