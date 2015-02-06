package fr.lyrgard.hexScape.camera;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class FlyByCamera implements AnalogListener, ActionListener {

    private Camera cam;
    private Vector3f initialUpVec;
    private float rotationSpeed = 5f;
    private float moveSpeed = 10f;
    private MotionAllowedListener motionAllowed = null;
    private boolean enabled = true;
    private boolean dragToRotate = true;
    private boolean canRotate = false;
    private InputManager inputManager;
    
    String[] mappings = new String[]{
            "FLYCAM_Left",
            "FLYCAM_Right",
            "FLYCAM_Up",
            "FLYCAM_Down",

            "FLYCAM_StrafeLeft",
            "FLYCAM_StrafeRight",
            "FLYCAM_Forward",
            "FLYCAM_Backward",

            "FLYCAM_RotateDrag",

            "FLYCAM_Rise",
            "FLYCAM_Lower"
        };
   
    /**
     * Creates a new FlyByCamera to control the given Camera object.
     * @param cam
     */
    public FlyByCamera(Camera cam){
        this.cam = cam;
        //initialUpVec = cam.getUp().clone();
        initialUpVec = Vector3f.UNIT_Y;
    }

    public void setMotionAllowedListener(MotionAllowedListener listener){
        this.motionAllowed = listener;
    }

    /**
     * Sets the move speed. The speed is given in world units per second.
     * @param moveSpeed
     */
    public void setMoveSpeed(float moveSpeed){
        this.moveSpeed = moveSpeed;
    }

    /**
     * Sets the rotation speed.
     * @param rotationSpeed
     */
    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * @param enable If false, the camera will ignore input.
     */
    public void setEnabled(boolean enable){
        enabled = enable;
    }

    /**
     * @return If enabled
     * @see FlyByCamera#setEnabled(boolean)
     */
    public boolean isEnabled(){
        return enabled;
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
     * Registers the FlyByCamera to recieve input events from the provided
     * Dispatcher.
     * @param dispacher
     */
    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;
       
//        inputManager.registerJoystickAxisBinding("FLYCAM_Left",  2, JoyInput.AXIS_X, true);
//        inputManager.registerJoystickAxisBinding("FLYCAM_Right", 2, JoyInput.AXIS_X, false);
//        inputManager.registerJoystickAxisBinding("FLYCAM_Up",    2, JoyInput.AXIS_Y, true);
//        inputManager.registerJoystickAxisBinding("FLYCAM_Down",  2, JoyInput.AXIS_Y, false);
//
//        inputManager.registerJoystickAxisBinding("FLYCAM_StrafeLeft",  2, JoyInput.POV_X, true);
//        inputManager.registerJoystickAxisBinding("FLYCAM_StrafeRight", 2, JoyInput.POV_X, false);
//        inputManager.registerJoystickAxisBinding("FLYCAM_Forward",     2, JoyInput.POV_Y, true);
//        inputManager.registerJoystickAxisBinding("FLYCAM_Backward",    2, JoyInput.POV_Y, false);

        

        // both mouse and button - rotation of cam
        inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(0, true));

        inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(0, false));

        inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(1, false));

        inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(1, true));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(1));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(AwtKeyInput.KEY_ADD));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(AwtKeyInput.KEY_SUBTRACT));
       

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate);
        
        initialUpVec = Vector3f.UNIT_Y;
    }
    
    public void unregisterInput(){
		if (this.inputManager == null) {
    		return;
    	}
    	
    	for (String s : mappings) {
    		if (this.inputManager.hasMapping(s)) {
    			this.inputManager.deleteMapping(s);
    		}
    	}
    	this.inputManager.removeListener(this);
	}

    private void rotateCamera(float value, Vector3f axis){
        if (dragToRotate){
            if (canRotate){
//                value = -value;
            }else{
                return;
            }
        }

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

    private void zoomCamera(float value){
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                  / (FastMath.DEG_TO_RAD * .5f);
        fovY += value * 0.1f;

        h = FastMath.tan( fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    private void riseCamera(float value){
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }

    private void moveCamera(float value, boolean sideways){
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways){
            cam.getLeft(vel);
        }else{
            cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }

    public void onAnalog(String name, float value, float tpf) {
        if (!enabled)
            return;

        if (name.equals("FLYCAM_Left")){
            rotateCamera(value, initialUpVec);
        }else if (name.equals("FLYCAM_Right")){
            rotateCamera(-value, initialUpVec);
        }else if (name.equals("FLYCAM_Up")){
            rotateCamera(-value, cam.getLeft());
        }else if (name.equals("FLYCAM_Down")){
            rotateCamera(value, cam.getLeft());
        }else if (name.equals("FLYCAM_Forward")){
            moveCamera(value, false);
        }else if (name.equals("FLYCAM_Backward")){
            moveCamera(-value, false);
        }else if (name.equals("FLYCAM_StrafeLeft")){
            moveCamera(value, true);
        }else if (name.equals("FLYCAM_StrafeRight")){
            moveCamera(-value, true);
        }else if (name.equals("FLYCAM_Rise")){
            riseCamera(value);
        }else if (name.equals("FLYCAM_Lower")){
            riseCamera(-value);
        }else if (name.equals("FLYCAM_ZoomIn")){
            zoomCamera(value);
        }else if (name.equals("FLYCAM_ZoomOut")){
            zoomCamera(-value);
        }
    }

    public void onAction(String name, boolean value, float tpf) {
        if (!enabled)
            return;

        if (name.equals("FLYCAM_RotateDrag") && dragToRotate){
            canRotate = value;
            inputManager.setCursorVisible(!value);
        }
    }

}
