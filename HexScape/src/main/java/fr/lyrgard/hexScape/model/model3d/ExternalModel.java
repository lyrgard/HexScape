package fr.lyrgard.hexScape.model.model3d;

import java.util.ArrayList;
import java.util.List;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ExternalModel {

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
		
		return node;
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
