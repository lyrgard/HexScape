package fr.lyrgard.hexScape.model.model3d;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.HexScapeCore;

public class TwoImagesExternalModel implements ExternalModel {

	private String name;
	
	private float height;
	private float width;
	
	private Material frontMaterial;
	private Material sideMaterial;	
	
	public TwoImagesExternalModel(String name, float height, float width, File frontImage, File sideImage) {
		this.name = name;
		this.height = height;
		this.width = width;
				
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		
		Texture frontTexture = assetManager.loadTexture(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(frontImage.toURI()).getPath());
		Texture sideTexture = assetManager.loadTexture(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(sideImage.toURI()).getPath());
		
		frontMaterial = getMaterial(frontTexture);
		sideMaterial = getMaterial(sideTexture);
	}

	private Material getMaterial(Texture texture) {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Material material = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		material.getAdditionalRenderState().setAlphaTest(true);
		material.getAdditionalRenderState().setAlphaFallOff(0.9f);
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		material.setBoolean("UseMaterialColors",true);
		material.setTexture("DiffuseMap", texture);
		material.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		material.setColor("Diffuse", new ColorRGBA(0.7f, 0.7f, 0.7f, 1));  // minimum material color
		material.setColor("Specular", new ColorRGBA(0, 0, 0, 1)); // for shininess
		material.setFloat("Shininess", 16f);
		material.setTransparent(true);
		
		
		return material;
	}

	@Override
	public Spatial getNewInstance() {
		
		
		
		Node modelNode = new Node(getName());
		
		Quad frontQuad = new Quad(width, height);
		Geometry frontGeometry = new Geometry(getName() + "-front" , frontQuad);
		frontGeometry.setQueueBucket(Bucket.Transparent);  
		frontGeometry.setShadowMode(ShadowMode.Cast);
		frontGeometry.setMaterial(frontMaterial);
//		Quad backQuad = new Quad(width, height);
//		Geometry backGeometry = new Geometry(getName() + "-back" , backQuad);
//		backGeometry.setQueueBucket(Bucket.Transparent);  
//		backGeometry.setShadowMode(ShadowMode.Off);
//		backGeometry.setMaterial(frontMaterial);
		
		Quad leftQuad = new Quad(width, height);
		Geometry leftGeometry = new Geometry(getName() + "-left" , leftQuad);
		leftGeometry.setQueueBucket(Bucket.Transparent);  
		leftGeometry.setShadowMode(ShadowMode.Cast);
		leftGeometry.setMaterial(sideMaterial);
//		Quad rightQuad = new Quad(width, height);
//		Geometry rightGeometry = new Geometry(getName() + "-right" , rightQuad);
//		rightGeometry.setQueueBucket(Bucket.Transparent);  
//		rightGeometry.setShadowMode(ShadowMode.Off);
//		rightGeometry.setMaterial(sideMaterial);
		
		frontGeometry.setLocalTranslation(-width/2, 0, 0);
		
//		backGeometry.setLocalRotation(Quaternion.IDENTITY.fromAngleAxis((float)Math.PI, Vector3f.UNIT_Y));
//		backGeometry.setLocalTranslation(width/2, 0, 0);
		
		leftGeometry.setLocalRotation(Quaternion.IDENTITY.fromAngleAxis((float)Math.PI/2, Vector3f.UNIT_Y));
		leftGeometry.setLocalTranslation(0, 0, width/2);
		
//		rightGeometry.setLocalRotation(Quaternion.IDENTITY.fromAngleAxis((float) (3 * Math.PI/2), Vector3f.UNIT_Y));
//		rightGeometry.setLocalTranslation(0, 0, -width/2);
		
		modelNode.attachChild(frontGeometry);
		//modelNode.attachChild(backGeometry);
		modelNode.attachChild(leftGeometry);
		//modelNode.attachChild(rightGeometry);
		
		return modelNode;
	}
	
//	private Mesh getMesh(float widthX, float widthZ, float height) {
//		Mesh mesh = new Mesh();
//		
//		float valueX = widthX / 2;
//		float valueZ = widthZ / 2;
//		
//		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(new Vector3f[] {new Vector3f(valueX, 0, valueZ), new Vector3f(-valueX, 0, -valueZ), new Vector3f(-valueX, height, -valueZ), new Vector3f(valueX, height, valueZ)}));
//		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1)}));
//		mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(new int[]{0, 1, 2, 0, 2, 3}));		
//		mesh.updateBound();
//		
//		return mesh;
//	}

	@Override
	public String getName() {
		return name;
	}

}
