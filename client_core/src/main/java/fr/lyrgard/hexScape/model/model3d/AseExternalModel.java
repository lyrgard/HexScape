package fr.lyrgard.hexScape.model.model3d;

import java.util.ArrayList;
import java.util.List;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class AseExternalModel implements ExternalModel {

	private List<MeshMaterialPair> meshMaterialPairs = new ArrayList<>();
	private String name;
	
	public static class MeshMaterialPair {
		private Mesh mesh;
		private Material mat;
		
		public MeshMaterialPair(Mesh mesh, Material mat) {
			super();
			this.mesh = mesh;
			this.mat = mat;
		}
		
		public Mesh getMesh() {
			return mesh;
		}
		public Material getMat() {
			return mat;
		}
	}
	
	public Spatial getNewInstance() {
		Node node = new Node(name);
		int i = 0;
		for (MeshMaterialPair pair : meshMaterialPairs) {
			Geometry geometry = new Geometry(name + i , pair.getMesh());
			geometry.setMaterial(pair.getMat());
			node.attachChild(geometry);
			i++;
		}
		
		//makeToonish(node);
		return node;
	}
	
	public void makeToonish(Spatial spatial){
        if (spatial instanceof Node){
            Node n = (Node) spatial;
            for (Spatial child : n.getChildren())
                makeToonish(child);
        }else if (spatial instanceof Geometry){
            Material mat = ((Geometry) spatial).getMaterial();
            mat.setBoolean("Toon", true);
            mat.setColor("EdgesColor", ColorRGBA.Red);
            mat.setFloat("EdgeSize", 0.01f);
            //mat.setColor("FogColor", new ColorRGBA(0.8f, 0.8f, 0.8f, 17.0f));
            //mat.setBoolean("Fog_Edges", true);
            
            //spatial.setMaterial(mat);
            //TangentBinormalGenerator.generate(spatial);
        }
    }


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<MeshMaterialPair> getMeshMaterialPairs() {
		return meshMaterialPairs;
	}
	
}
