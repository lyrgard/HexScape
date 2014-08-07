package fr.lyrgard.hexScape.model.model3d.loader;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.ObjExternalModel;

public class ObjModelLoader implements ModelLoader {

	@Override
	public boolean canLoad(String name) {
		return getObjFile(name).exists();
	}

	@Override
	public ExternalModel load(String name) {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Spatial model = assetManager.loadModel(ModelLoader.BASE_FOLDER + name + "/" + name + ".obj");
		
		if (model instanceof Geometry) {
			((Geometry)model).getMaterial().setColor("Ambient", ColorRGBA.White);
		} else {
			for (Spatial child : ((Node)model).getChildren()) {
				if (child instanceof Geometry) {
					((Geometry)child).getMaterial().setColor("Ambient", ColorRGBA.White.mult(0.2f));
				}
			}
		}
		
		ObjExternalModel externalModel = new ObjExternalModel();
		externalModel.setName(name);
		externalModel.setSpatial(model);
		
		return externalModel;
	}

	
	private File getObjFile(String name) {
		File folder = new File(ModelLoader.BASE_FOLDER, name);
		return new File(folder, name + ".obj");
	}
}
