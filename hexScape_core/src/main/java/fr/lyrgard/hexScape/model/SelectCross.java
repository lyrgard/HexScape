package fr.lyrgard.hexScape.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.model3d.TileMesh;

public class SelectCross implements Displayable {
	
	private static Material material;
	
	private static Mesh mesh;
	
	private Geometry geometry;
	
	static {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		Texture tileTexture = assetManager.loadTexture("model/texture/select_cross.png");
			
		material = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors",true);
		material.setTexture("DiffuseMap", tileTexture);
		material.setColor("Ambient", ColorRGBA.White);
		material.setColor("Diffuse",ColorRGBA.White);  // minimum material color
		material.setColor("Specular",ColorRGBA.White); // for shininess
		material.setFloat("Shininess", 50f);
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		mesh = new Mesh();
		
		float value = TileMesh.HEX_SIZE_Z / 2;
		
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(new Vector3f[] {new Vector3f(-value, 0, value), new Vector3f(value, 0, value), new Vector3f(value, 0, -value), new Vector3f(-value, 0, -value)}));
		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1)}));
		mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(new int[]{0, 1, 2, 0, 2, 3}));		
		mesh.updateBound();
	}
	
	public SelectCross() {
		
		geometry = new Geometry("selectCross", mesh);
		geometry.setQueueBucket(Bucket.Translucent);  
		geometry.setShadowMode(ShadowMode.Off);
		geometry.setMaterial(material);
	}
	
	@Override
	public Spatial getSpatial() {
		return geometry;
	}

}
