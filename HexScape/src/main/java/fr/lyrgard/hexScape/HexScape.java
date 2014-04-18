package fr.lyrgard.hexScape;
import java.io.File;


import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;

import fr.lyrgard.hexScape.camera.FlyCamAppState;
import fr.lyrgard.hexScape.model.model3d.MapLoader;



public class HexScape extends SimpleApplication {

	public static void main(String[] args){
		HexScape app = new HexScape();
		app.start(); // start the game
	}
	
	public HexScape() {
		super(new AppState[] { new StatsAppState(), new FlyCamAppState(), new DebugKeysAppState() });
	}

	@Override
	public void simpleInitApp() {
		
		assetManager.registerLocator("", FileLocator.class);
		
//		VirtualScapeMapReader mapReader = new VirtualScapeMapReader();
//		Map map = mapReader.readMap(new File("C:/test.hsc"));
//		
//		MapMesh mesh = new MapMesh();
//		mesh.setMap(map);
//		mesh.update();
//
//		Geometry geo = new Geometry("map", mesh);
//		Material mat = new Material(assetManager, 
//				"Common/MatDefs/Misc/Unshaded.j3md");
//		
//		Texture TileTexture = assetManager.loadTexture(
//		        "model/texture/tile/TileTexture.jpg");
//		mat.setTexture("ColorMap", TileTexture);
//		geo.setMaterial(mat);
//		rootNode.attachChild(geo);
		
		cam.setLocation(new Vector3f(0, 10, 10));
		cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		
		MapLoader mapLoader = new MapLoader(assetManager);
		Node mapNode = mapLoader.getMap(new File("c:/test.hsc"));
		
		rootNode.attachChild(mapNode);
		
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
        
        rootNode.setShadowMode(ShadowMode.CastAndReceive);
        
//        FilterPostProcessor  fpp = new FilterPostProcessor(assetManager);
//        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
//        fpp.addFilter(ssaoFilter);
//        viewPort.addProcessor(fpp);

	}

}
