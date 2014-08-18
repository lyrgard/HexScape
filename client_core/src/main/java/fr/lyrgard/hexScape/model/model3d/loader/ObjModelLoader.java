package fr.lyrgard.hexScape.model.model3d.loader;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.ObjExternalModel;

public class ObjModelLoader extends AbstractModelLoader {

	@Override
	public boolean canLoad(String name) {
		File file = getObjFile(name);
		return file != null && file.exists();
	}

	@Override
	public ExternalModel load(String name) {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Spatial model = assetManager.loadModel(getObjFile(name).getPath().replaceAll("\\\\", "/"));
		
		if (model instanceof Geometry) {
			Material mat = ((Geometry)model).getMaterial(); 
			mat.setColor("Ambient", ColorRGBA.White);
			mat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
	        mat.setColor("Specular",ColorRGBA.White); // for shininess
	        mat.setFloat("Shininess", 50f);
		} else {
			for (Spatial child : ((Node)model).getChildren()) {
				if (child instanceof Geometry) {
					Material mat = ((Geometry)child).getMaterial(); 
					mat.setColor("Ambient", ColorRGBA.White);
					mat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
			        mat.setColor("Specular",ColorRGBA.White); // for shininess
			        mat.setFloat("Shininess", 50f);
				}
			}
		}
		
		ObjExternalModel externalModel = new ObjExternalModel();
		externalModel.setName(name);
		externalModel.setSpatial(model);
		
		return externalModel;
	}

	
	private File getObjFile(String name) {
		File file = null;
		for (File folder : getModelsFolders()) {
			File potentialFile = new File(new File(folder, name), name + ".obj");
			if (potentialFile.exists()) {
				file = potentialFile;
			}
		}
		return file;
	}
}
