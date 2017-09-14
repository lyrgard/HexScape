package fr.lyrgard.hexScape.model;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.service.MapManager;

public class Table extends Geometry {
	
	static private Texture wood;
	
	static private Material tableMat;
	
	public Table(MapManager scene) {
		super("Table");
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager(); 
		if (wood == null) {
			wood = assetManager.loadTexture("model/texture/wood.jpg");
		}
		
		BoundingBox bv = (BoundingBox)scene.getSpatial().getWorldBound();
		float sizeX = bv.getXExtent() * 2f;
		float sizeZ = bv.getZExtent() * 2f;
		Vector3f center = bv.getCenter();
		Box tableMesh = new Box(sizeX , 1, sizeZ);

		setMesh(tableMesh);
		setLocalTranslation(new Vector3f(center.x,-1,center.z));

		if (tableMat == null) {
			tableMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			tableMat.setTexture("DiffuseMap", wood);
			tableMat.setBoolean("UseMaterialColors",true);    
			tableMat.setColor("Ambient", ColorRGBA.White);
			tableMat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
			tableMat.setColor("Specular",ColorRGBA.White); // for shininess
			tableMat.setFloat("Shininess", 1f);
		}
		setMaterial(tableMat);
		setShadowMode(ShadowMode.Receive);
	}

}
