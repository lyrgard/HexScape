package fr.lyrgard.hexScape;



import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.camera.FlyByCameraAppState;
import fr.lyrgard.hexScape.camera.PointOfViewCameraAppState;
import fr.lyrgard.hexScape.camera.RotatingAroundCameraAppState;
import fr.lyrgard.hexScape.control.PieceControlerAppState;
import fr.lyrgard.hexScape.control.SelectMarkerAppState;
import fr.lyrgard.hexScape.control.TitleMenuButtonsAppState;
import fr.lyrgard.hexScape.control.View3dControlState;
import fr.lyrgard.hexScape.message.CoreReady;
import fr.lyrgard.hexScape.message.LookingFreelyMessage;
import fr.lyrgard.hexScape.message.LookingFromAboveMessage;
import fr.lyrgard.hexScape.message.LookingFromPieceMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.TitleScreen;
import fr.lyrgard.hexScape.service.MapManager;
import fr.lyrgard.hexScape.service.PieceManager;

public class HexScapeJme3Application extends SimpleApplication {
	
	private MapManager scene;
	
	private PieceManager pieceLookedAt;
	
	private RotatingAroundCameraAppState rotatingAroundCameraAppState = new RotatingAroundCameraAppState();
	
	private PointOfViewCameraAppState pointOfViewCameraAppState = new PointOfViewCameraAppState();
	
	private FlyByCameraAppState flyByCameraAppState = new FlyByCameraAppState();
	
	private PieceControlerAppState pieceControlerAppState = new PieceControlerAppState();
	
	private TitleMenuButtonsAppState titleMenuButtonsAppState = new TitleMenuButtonsAppState();
	
	private SelectMarkerAppState selectMarkerAppState = new SelectMarkerAppState();
	
	PointLight haloLight;
	
	public HexScapeJme3Application() {
		super(new AppState[] {});
		
		//stateManager.attach(new StatsAppState());
		stateManager.attach(rotatingAroundCameraAppState);
		stateManager.attach(pieceControlerAppState);
		stateManager.attach(titleMenuButtonsAppState);
		stateManager.attach(pointOfViewCameraAppState);
		stateManager.attach(selectMarkerAppState);
		stateManager.attach(flyByCameraAppState);
		
		pieceControlerAppState.setEnabled(false);
		rotatingAroundCameraAppState.setEnabled(false);
		pointOfViewCameraAppState.setEnabled(false);
		selectMarkerAppState.setEnabled(false);
		flyByCameraAppState.setEnabled(false);
	}

	@Override
	public void simpleInitApp() {
		
		assetManager.registerLocator(HexScapeCore.APP_DATA_FOLDER.getAbsolutePath(), FileLocator.class);
		
		float aspect = (float)cam.getWidth() / (float)cam.getHeight();
		cam.setFrustumPerspective( 45f, aspect, 0.1f, cam.getFrustumFar() );
		
		rotatingAroundCameraAppState.setRotateAroundNode(null);
		
		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White.mult(0.5f));
		sun.setDirection(new Vector3f(1,-1, 0.5f).normalizeLocal());
		rootNode.addLight(sun);
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1f));
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
        GuiMessageBus.post(new CoreReady());
	}

	public MapManager getScene() {
		return scene;
	}
	
	public void setControlState(View3dControlState state) {
		switch(state) {
		case TITLE_SCREEN:
			pointOfViewCameraAppState.setEnabled(false);
			pieceControlerAppState.setEnabled(false);
			rotatingAroundCameraAppState.setEnabled(false);
			selectMarkerAppState.setEnabled(false);
			flyByCameraAppState.setEnabled(false);
			
			titleMenuButtonsAppState.setEnabled(true);
			cam.setLocation(new Vector3f(0, 100, 0));
			cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
			break;
		case SHOW_MAP_OBSERVE:
			pointOfViewCameraAppState.setEnabled(false);
			titleMenuButtonsAppState.setEnabled(false);
			pieceControlerAppState.setEnabled(false);
			flyByCameraAppState.setEnabled(false);
			
			rotatingAroundCameraAppState.setEnabled(true);
			selectMarkerAppState.setEnabled(true);
			rotatingAroundCameraAppState.setRotateAroundNode(scene.getSpatial());
			break;
		case SHOW_MAP_PLAYING:
			pointOfViewCameraAppState.setEnabled(false);
			titleMenuButtonsAppState.setEnabled(false);
			flyByCameraAppState.setEnabled(false);
			
			pieceControlerAppState.setEnabled(true);
			selectMarkerAppState.setEnabled(true);
			rotatingAroundCameraAppState.setEnabled(true);
			rotatingAroundCameraAppState.setRotateAroundNode(scene.getSpatial());
			break;
		case SHOW_PIECE_VIEW:
			titleMenuButtonsAppState.setEnabled(false);
			pieceControlerAppState.setEnabled(false);
			rotatingAroundCameraAppState.setEnabled(false);
			flyByCameraAppState.setEnabled(false);
			
			pointOfViewCameraAppState.setEnabled(true);
			selectMarkerAppState.setEnabled(true);
			pointOfViewCameraAppState.setPiece(pieceLookedAt);
			break;
		case FREE_MOVING:
			pointOfViewCameraAppState.setEnabled(false);
			titleMenuButtonsAppState.setEnabled(false);
			rotatingAroundCameraAppState.setEnabled(false);
			
			pieceControlerAppState.setEnabled(true);
			selectMarkerAppState.setEnabled(true);
			flyByCameraAppState.setEnabled(true);
			break;
		default:
			break;
		}
	}
	
	public void displayTitleScreen() {
		
		if (this.scene != null) {
			rootNode.detachChild(this.scene.getSpatial());
			scene = null;
		}
		setControlState(View3dControlState.TITLE_SCREEN);
		
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
			//rootNode.attachChild(Sky.getInstance().getSpatial());
			if (CurrentUserInfo.getInstance().isPlayingGame()) {
				setControlState(View3dControlState.SHOW_MAP_PLAYING);
			} else {
				setControlState(View3dControlState.SHOW_MAP_OBSERVE);
			}
		} else {
			rotatingAroundCameraAppState.setRotateAroundNode(null);
		}
	}
	
	public void lookThroughEyesOf(PieceManager piece) {
		if (piece != null) {
			pieceLookedAt = piece;
			
			setControlState(View3dControlState.SHOW_PIECE_VIEW);
			
			GuiMessageBus.post(new LookingFromPieceMessage(CurrentUserInfo.getInstance().getPlayerId(), piece.getPiece().getId()));
		}
	}
	
	public void lookAtTheMap() {
		if (scene != null) {
			pieceLookedAt = null;
			
			if (CurrentUserInfo.getInstance().isPlayingGame()) {
				setControlState(View3dControlState.SHOW_MAP_PLAYING);
			} else {
				setControlState(View3dControlState.SHOW_MAP_OBSERVE);
			}
			
			GuiMessageBus.post(new LookingFromAboveMessage(CurrentUserInfo.getInstance().getPlayerId()));
		}
	}
	
	public void lookFreely() {
		if (scene != null) {
			setControlState(View3dControlState.FREE_MOVING);
			GuiMessageBus.post(new LookingFreelyMessage(CurrentUserInfo.getInstance().getPlayerId()));
		}
	}

	public PieceControlerAppState getPieceControlerAppState() {
		return pieceControlerAppState;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

}
