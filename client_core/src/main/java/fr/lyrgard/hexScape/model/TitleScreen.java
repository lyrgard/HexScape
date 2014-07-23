package fr.lyrgard.hexScape.model;

import java.io.InputStream;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.TitleScreenButton.Type;
import fr.lyrgard.hexScape.service.MapManager;

public class TitleScreen implements Displayable {
	
	private static final TitleScreen INSTANCE = new TitleScreen();
	
	public static TitleScreen getInstance() {
		return INSTANCE;
	}

	
	private Node node;
	
	private Node buttons;
	
	private TitleScreen() {
		
	}
	
	@Override
	public Spatial getSpatial() {
		if (node == null) {
			node = new Node();
			buttons = new Node();
			node.attachChild(buttons);
			
			InputStream stream = TitleScreen.class.getResourceAsStream("/title/Logo.hsc");
			MapManager map = MapManager.fromInputStream(stream);
			Spatial mapSpatial = map.getSpatial();
			
			mapSpatial.setLocalRotation(new Quaternion().fromAngleAxis(210 * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y));
			BoundingVolume bv = mapSpatial.getWorldBound();
			mapSpatial.setLocalTranslation(-bv.getCenter().x, 0, -bv.getCenter().z);
			
			node.attachChild(mapSpatial);
			//node.attachChild(SkyFactory.createSky(HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager(), "title/background.jpg", true));
			
			//Texture tileTexture = assetManager.loadTexture("model/texture/select_cross_white.png");
//	        Material bgMaterial = new Material(assetManager, 
//					"Common/MatDefs/Light/Lighting.j3md");
//	        bgMaterial.setBoolean("UseMaterialColors",true);
//	        bgMaterial.setTexture("DiffuseMap", tileTexture);
//	        bgMaterial.setColor("Ambient", ColorRGBA.Red);
//	        bgMaterial.setColor("Diffuse",ColorRGBA.White);  // minimum material color
//	        bgMaterial.setColor("Specular",ColorRGBA.White); // for shininess
//	        bgMaterial.setFloat("Shininess", 50f);
			AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
			
			
			
			float x = 39;
			float y = 30;
			buttons.attachChild(new TitleScreenButton(Type.CONFIG, "title/config.png", x, y));
			
			x = 20.1f;
			y = -19.5f;
			buttons.attachChild(new TitleScreenButton(Type.MULTIPLAYER, "title/multiplayer.png", x, y));
			
			
			x = -15.8f;
			y = 23.6f;
			buttons.attachChild(new TitleScreenButton(Type.SOLO, "title/solo.png", x, y));
			
			
		
			
			Material backgroundMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			Texture backgroundTex = assetManager.loadTexture("title/background.jpg");
			backgroundMat.setTexture("ColorMap", backgroundTex);
			Quad fsq = new Quad(200, 125, false);
			Geometry backgroundGeom = new Geometry("Background", fsq);
			backgroundGeom.setQueueBucket(Bucket.Sky);
			backgroundGeom.setCullHint(CullHint.Never);
			backgroundGeom.setMaterial(backgroundMat);
			backgroundGeom.setLocalRotation(new Quaternion().fromAngleAxis(-90 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
			backgroundGeom.setLocalTranslation(-100,-2, +75);  //Need to Divide by two because the quad origin is bottom left
			//backgroundGeom.setLocalTranslation(0, -200, 0);
			
			
			node.attachChild(backgroundGeom);
		}
		return node;
	}

	public Node getButtons() {
		return buttons;
	}


}
