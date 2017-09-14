package fr.lyrgard.hexScape;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import fr.lyrgard.hexScape.camera.FlyByCameraAppState;
import fr.lyrgard.hexScape.camera.PointOfViewCameraAppState;
import fr.lyrgard.hexScape.camera.RotatingAroundCameraAppState;
import fr.lyrgard.hexScape.control.FocusControllerAppState;
import fr.lyrgard.hexScape.control.PieceControlerAppState;
import fr.lyrgard.hexScape.control.SelectMarkerAppState;
import fr.lyrgard.hexScape.control.TitleMenuButtonsAppState;
import fr.lyrgard.hexScape.model.Sky;
import fr.lyrgard.hexScape.model.Table;
import fr.lyrgard.hexScape.service.ConfigurationService;
import fr.lyrgard.hexScape.service.MapManager;

public class Test extends SimpleApplication {

	private FlyByCameraAppState flyByCameraAppState = new FlyByCameraAppState();
	private RotatingAroundCameraAppState rotatingAroundCameraAppState = new RotatingAroundCameraAppState();
	
	private PointOfViewCameraAppState pointOfViewCameraAppState = new PointOfViewCameraAppState();
	
	
	private TitleMenuButtonsAppState titleMenuButtonsAppState = new TitleMenuButtonsAppState();
	
	private FocusControllerAppState focusControllerAppState = new FocusControllerAppState();
	
	private SelectMarkerAppState selectMarkerAppState = new SelectMarkerAppState();
	
	private static DirectionalLightShadowRenderer dlsr;
	
	private static DirectionalLight sun;
	
	public static void main(String... args) {
		
		final Test app = new Test();
		AppSettings settings = new AppSettings(true);
		//settings.setCustomRenderer(SwingContext.class);
		settings.setFrameRate(60);
		
		// Antialiasing
		//settings.setSamples(4);
		
		
		app.setShowSettings(false);
		app.setSettings(settings);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createCanvas();
				
				final JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
				ctx.setSystemListener(app);
				Dimension dim = new Dimension(640, 480);
				ctx.getCanvas().setPreferredSize(dim);
				
				JFrame frame = new JFrame();
				JPanel jPanel = new JPanel();
				final JPanel jPanel2 = new JPanel();
				JButton button = new JButton("switch");
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						jPanel2.add(ctx.getCanvas());
						app.reinitShadows();
					}
				});
				jPanel.add(ctx.getCanvas());
				JPanel container = new JPanel();
				frame.add(container);
				container.add(jPanel);
				container.add(button);
				container.add(jPanel2);
				frame.setVisible(true);
				
//				app.getHexScapeJme3Application().createCanvas();
//				
//				final SwingContext ctx = (SwingContext)app.getHexScapeJme3Application().getContext();
//				
//				ctx.setSystemListener(app.getHexScapeJme3Application());
//				
//				final Canvas panel3d = ctx.getCanvas();
//				
//				Dimension dim = new Dimension(102, 77);
//				panel3d.setMinimumSize(dim);
//				panel3d.setEnabled(false);
//				
//				app.getHexScapeJme3Application().startCanvas();
//				
//		        new HexScapeFrame(new View3d(panel3d));
		        
		        //JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			}
		});
		
//		app.start();
	}
	
	public Test() {
		super(new AppState[] {});
		
		stateManager.attach(new StatsAppState());
		stateManager.attach(rotatingAroundCameraAppState);
		stateManager.attach(pointOfViewCameraAppState);
		stateManager.attach(selectMarkerAppState);
		stateManager.attach(flyByCameraAppState);
		stateManager.attach(focusControllerAppState);
		
		rotatingAroundCameraAppState.setEnabled(false);
		pointOfViewCameraAppState.setEnabled(false);
		selectMarkerAppState.setEnabled(false);
		flyByCameraAppState.setEnabled(true);
	}
	
	@Override
	public void simpleInitApp() {
		HexScapeCore.getInstance().getHexScapeJme3Application().setAssetManager(assetManager);
		assetManager.registerLocator(HexScapeCore.APP_DATA_FOLDER.getAbsolutePath(), FileLocator.class);
		
		float aspect = (float)cam.getWidth() / (float)cam.getHeight();
		cam.setFrustumPerspective( 45f, aspect, 0.1f, cam.getFrustumFar() );
		
		Vector3f sunDirection = new Vector3f(-0.5f,-1, -0.5f).normalizeLocal();
		
		sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White.mult(0.8f));
		sun.setDirection(sunDirection.normalizeLocal());
		rootNode.addLight(sun);
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1f));
		rootNode.addLight(al);
		
		
		reinitShadows();
		
		ConfigurationService.getInstance().setGameFolder("heroscape");
		final File file = new File("D:/Users/egqss/Documents/H/Les Orgues de Batailles.hsc");
		
		final MapManager mapManager = MapManager.fromFile(file);
        rootNode.attachChild(mapManager.getSpatial());
        rootNode.attachChild(Sky.getInstance().getSpatial());
        rootNode.attachChild(new Table(mapManager));
	}
	
	public void reinitShadows() {
		final int SHADOWMAP_SIZE=1024;
		if (dlsr != null) {
			viewPort.removeProcessor(dlsr);
		}
		dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.5f);
        viewPort.addProcessor(dlsr);
	}

}
