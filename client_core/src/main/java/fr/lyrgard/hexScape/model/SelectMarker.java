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
import fr.lyrgard.hexScape.model.player.ColorEnum;

public class SelectMarker implements Displayable {
	
	private static Material redMaterial;
	
	private static Material blueMaterial;
	
	private static Material greenMaterial;
	
	private static Mesh mesh;
	
	private Geometry geometry;
	
	static {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		Texture tileTexture = assetManager.loadTexture("model/texture/select_cross_white.png");
			
		redMaterial = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		redMaterial.setBoolean("UseMaterialColors",true);
		redMaterial.setTexture("DiffuseMap", tileTexture);
		redMaterial.setColor("Ambient", ColorRGBA.Red);
		redMaterial.setColor("Diffuse",ColorRGBA.White);  // minimum material color
		redMaterial.setColor("Specular",ColorRGBA.White); // for shininess
		redMaterial.setFloat("Shininess", 50f);
		redMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		blueMaterial = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		blueMaterial.setBoolean("UseMaterialColors",true);
		blueMaterial.setTexture("DiffuseMap", tileTexture);
		blueMaterial.setColor("Ambient", ColorRGBA.Blue);
		blueMaterial.setColor("Diffuse",ColorRGBA.White);  // minimum material color
		blueMaterial.setColor("Specular",ColorRGBA.White); // for shininess
		blueMaterial.setFloat("Shininess", 50f);
		blueMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		greenMaterial = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		greenMaterial.setBoolean("UseMaterialColors",true);
		greenMaterial.setTexture("DiffuseMap", tileTexture);
		greenMaterial.setColor("Ambient", ColorRGBA.Green);
		greenMaterial.setColor("Diffuse",ColorRGBA.White);  // minimum material color
		greenMaterial.setColor("Specular",ColorRGBA.White); // for shininess
		greenMaterial.setFloat("Shininess", 50f);
		greenMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		
		mesh = new Mesh();
		
		float value = TileMesh.HEX_SIZE_Z / 2;
		
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(new Vector3f[] {new Vector3f(-value, 0, value), new Vector3f(value, 0, value), new Vector3f(value, 0, -value), new Vector3f(-value, 0, -value)}));
		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1)}));
		mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(new int[]{0, 1, 2, 0, 2, 3}));		
		mesh.updateBound();
	}
	
	public SelectMarker(ColorEnum color) {
		
		geometry = new Geometry("selectCross", mesh);
		geometry.setQueueBucket(Bucket.Translucent);  
		geometry.setShadowMode(ShadowMode.Off);
		switch (color) {
		case BLUE:
			geometry.setMaterial(blueMaterial);
			break;
		case RED:
			geometry.setMaterial(redMaterial);
			break;
		case GREEN:
			geometry.setMaterial(greenMaterial);
			break;
		default:
			geometry.setMaterial(redMaterial);
			break;
		}
	}
	
	@Override
	public Spatial getSpatial() {
		return geometry;
	}

}
