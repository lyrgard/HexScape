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
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;

public class TitleScreenSprite extends Geometry {
	
	private static final Quaternion ROTATION = new Quaternion().fromAngleAxis(90 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X).mult(new Quaternion().fromAngleAxis(180 * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y));
	
	private float x;
	private float y;
	private float sizeX;
	private float sizeY;
	private Type type;
	private TitleScreenSprite label;
	private Node labelNode;
	
	public enum Type {
		SOLO, MULTIPLAYER, CONFIG, SPRITE;
	}

	public TitleScreenSprite(Type type, String image, float x, float y, float sizeX, float sizeY, TitleScreenSprite label, Node labelNode) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.label = label;
		this.labelNode = labelNode;
		
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Material configMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture configTex = assetManager.loadTexture(image);
		configMat.setTexture("ColorMap", configTex);
		configMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		Quad quad = new Quad(sizeX, sizeY, false);
		setMesh(quad);
		setMaterial(configMat);
		setQueueBucket(Bucket.Translucent);  
		setShadowMode(ShadowMode.Off);
		setLocalRotation(ROTATION);
		setLocalTranslation(-x + sizeX/2, 1, y - sizeY/2);
	}
	
	public Type getType() {
		return type;
	}

	public void selected() {
		float scale = 1.1f;
		setLocalScale(scale);
		float newSize = sizeX * scale;
		setLocalTranslation(-x + newSize/2, 1, y - newSize/2);
		if (labelNode != null && label != null) {
			labelNode.attachChild(label);
		}
	}
	
	public void notSelected() {
		setLocalScale(1f);
		setLocalTranslation(-x + sizeX/2, 1, y - sizeY/2);
		if (labelNode != null && label != null) {
			labelNode.detachChild(label);
		}
	}
	
	
}
