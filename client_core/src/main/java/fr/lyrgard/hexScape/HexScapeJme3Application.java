package fr.lyrgard.hexScape;


import java.awt.Color;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.ui.Picture;

import fr.lyrgard.hexScape.camera.RotatingAroundCameraAppState;
import fr.lyrgard.hexScape.control.PieceControlerAppState;
import fr.lyrgard.hexScape.control.TitleMenuButtonsAppState;
import fr.lyrgard.hexScape.model.TitleScreen;
import fr.lyrgard.hexScape.service.MapManager;

public class HexScapeJme3Application extends SimpleApplication {
	
	private MapManager scene;
	
	private RotatingAroundCameraAppState rotatingAroundCameraAppState = new RotatingAroundCameraAppState();
	
	private PieceControlerAppState pieceControlerAppState = new PieceControlerAppState();
	
	private TitleMenuButtonsAppState titleMenuButtonsAppState = new TitleMenuButtonsAppState();
	
	PointLight haloLight;
	
	public HexScapeJme3Application() {
		super(new AppState[] {});
		
		//stateManager.attach(new StatsAppState());
		stateManager.attach(rotatingAroundCameraAppState);
		stateManager.attach(pieceControlerAppState);
		stateManager.attach(titleMenuButtonsAppState);
		pieceControlerAppState.setEnabled(false);
		rotatingAroundCameraAppState.setEnabled(false);
	}

	@Override
	public void simpleInitApp() {
		
		assetManager.registerLocator("", FileLocator.class);
		
		cam.setLocation(new Vector3f(0, 100, 0));
		cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
	
		rotatingAroundCameraAppState.setRotateAroundNode(null);
		
		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White.mult(0.5f));
		sun.setDirection(new Vector3f(1,-1, 0.5f).normalizeLocal());
		rootNode.addLight(sun);
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.8f));
		rootNode.addLight(al);
		
		final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.3f);
        viewPort.addProcessor(dlsr);
        //viewPort.setBackgroundColor(ColorRGBA.Blue);
        
//        p = new Picture("background");
//        Texture tileTexture = assetManager.loadTexture("model/texture/select_cross_white.png");
//        Material bgMaterial = new Material(assetManager, 
//				"Common/MatDefs/Light/Lighting.j3md");
//        bgMaterial.setBoolean("UseMaterialColors",true);
//        bgMaterial.setTexture("DiffuseMap", tileTexture);
//        bgMaterial.setColor("Ambient", ColorRGBA.Red);
//        bgMaterial.setColor("Diffuse",ColorRGBA.White);  // minimum material color
//        bgMaterial.setColor("Specular",ColorRGBA.White); // for shininess
//        bgMaterial.setFloat("Shininess", 50f);
//        p.setMaterial(bgMaterial);
//        
//       
//		ViewPort pv = renderManager.createPreView("background", cam);
//		pv.setClearFlags(true, true, true);
//		pv.attachScene(p);
		 
//		viewPort.setClearFlags(false, true, true);
        
        rootNode.setShadowMode(ShadowMode.CastAndReceive);
        setPauseOnLostFocus(false);
        
        displayTitleScreen();
        
        //rotatingAroundCameraAppState.setRotateAroundNode(TitleScreen.getInstance().getSpatial());
	}

	public MapManager getScene() {
		return scene;
	}
	
	public void displayTitleScreen() {
		
		if (this.scene != null) {
			rootNode.detachChild(this.scene.getSpatial());
			scene = null;
		}
		
		titleMenuButtonsAppState.setEnabled(true);
		pieceControlerAppState.setEnabled(false);
		rotatingAroundCameraAppState.setEnabled(false);
		cam.setLocation(new Vector3f(0, 100, 0));
		cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		rootNode.attachChild(TitleScreen.getInstance().getSpatial());
	}

	public void setScene(MapManager scene) {
		
		rootNode.detachChild(TitleScreen.getInstance().getSpatial());
		if (this.scene != null) {
			rootNode.detachChild(this.scene.getSpatial());
		}
		
		this.scene = scene;
		
		if (scene != null) {
			rootNode.attachChild(scene.getSpatial());
			titleMenuButtonsAppState.setEnabled(false);
			pieceControlerAppState.setEnabled(true);
			rotatingAroundCameraAppState.setEnabled(true);
			rotatingAroundCameraAppState.setRotateAroundNode(scene.getSpatial());
		} else {
			rotatingAroundCameraAppState.setRotateAroundNode(null);
		}
	}

	public PieceControlerAppState getPieceControlerAppState() {
		return pieceControlerAppState;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

}
