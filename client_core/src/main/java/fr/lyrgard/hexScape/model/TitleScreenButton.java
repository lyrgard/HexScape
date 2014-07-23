package fr.lyrgard.hexScape.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;

public class TitleScreenButton extends Geometry {
	
	private static final float SIZE = 12.12435f;
	
	private float x;
	private float y;
	private Type type;
	
	public enum Type {
		SOLO, MULTIPLAYER, CONFIG;
	}

	public TitleScreenButton(Type type, String image, float x, float y) {
		this.x = x;
		this.y = y;
		this.type = type;
		
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Material configMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture configTex = assetManager.loadTexture(image);
		configMat.setTexture("ColorMap", configTex);
		configMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		Quad config = new Quad(SIZE, SIZE, true);
		setMesh(config);
		setMaterial(configMat);
		setQueueBucket(Bucket.Translucent);  
		setShadowMode(ShadowMode.Off);
		setLocalRotation(new Quaternion().fromAngleAxis(-90 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		setLocalTranslation(-SIZE/2 - x, 1, +SIZE/2 - y);
	}
	
	public Type getType() {
		return type;
	}

	public void selected() {
		float scale = 1.1f;
		setLocalScale(scale);
		float newSize = SIZE * scale;
		setLocalTranslation(-newSize/2 - x, 1, +newSize/2 - y);
	}
	
	public void notSelected() {
		setLocalScale(1f);
		setLocalTranslation(-SIZE/2 - x, 1, +SIZE/2 - y);
	}
}
