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
import fr.lyrgard.hexScape.model.TitleScreenSprite.Type;
import fr.lyrgard.hexScape.service.MapManager;

public class TitleScreen implements Displayable {
	
	private static final float BUTTON_SIZE = 12.12435f;
	
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
			populateNode();
		}
		return node;
	}
	
	public void populateNode() {
		node.detachAllChildren();
		buttons = new Node();
		node.attachChild(buttons);
		
		InputStream stream = TitleScreen.class.getResourceAsStream("/title/Logo.hsc");
		MapManager map = MapManager.fromInputStream(stream);
		Spatial mapSpatial = map.getSpatial();
		
		
		mapSpatial.setLocalRotation(new Quaternion().fromAngleAxis(210 * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y));
		BoundingVolume bv = mapSpatial.getWorldBound();
		mapSpatial.setLocalTranslation(-bv.getCenter().x, 0, -bv.getCenter().z);
		
		node.attachChild(mapSpatial);
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		float sizeX = 13.15f;
		float sizeY = 12;
		float x = 38 + BUTTON_SIZE/2 + sizeX/2 + 1;
		float y = -28f;
		TitleScreenSprite configLabel = new TitleScreenSprite(Type.SPRITE, "title/configLabel.png", x, y, sizeX, sizeY, null, null);
		x = 38;
		y = y;
		buttons.attachChild(new TitleScreenSprite(Type.CONFIG, "title/config.png", x, y, BUTTON_SIZE, BUTTON_SIZE, configLabel, node));
		
		sizeX = 30;
		sizeY = 8;
		x = 20f - BUTTON_SIZE/2 - sizeX/2;
		y = 21f;
		TitleScreenSprite multiplayerLabel = new TitleScreenSprite(Type.SPRITE, "title/multiplayerLabel.png", x, y, sizeX, sizeY, null, null);
		x = 20f;
		y = 19.4f;
		buttons.attachChild(new TitleScreenSprite(Type.MULTIPLAYER, "title/multiplayer.png", x, y, BUTTON_SIZE, BUTTON_SIZE, multiplayerLabel, node));
		
		sizeX = 30;
		sizeY = 8;
		x = -15.5f + BUTTON_SIZE/2 + sizeX/2;
		y = -26.5f;
		TitleScreenSprite soloLabel = new TitleScreenSprite(Type.SPRITE, "title/soloLabel.png", x, y, sizeX, sizeY, null, null);
		x = -15.5f;
		y = -23.6f;
		buttons.attachChild(new TitleScreenSprite(Type.SOLO, "title/solo.png", x, y, BUTTON_SIZE, BUTTON_SIZE, soloLabel, node));
		
		x=17;
		y=-14;
		node.attachChild(new TitleScreenSprite(Type.SPRITE, "title/subtitle.png", x, y, 16, 9, null, null));
	
		
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

	public Node getButtons() {
		return buttons;
	}


}
