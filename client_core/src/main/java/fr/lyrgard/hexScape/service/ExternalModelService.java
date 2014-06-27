package fr.lyrgard.hexScape.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.loader.AseModelLoader;
import fr.lyrgard.hexScape.model.model3d.loader.ModelLoader;
import fr.lyrgard.hexScape.model.model3d.loader.ObjModelLoader;

public class ExternalModelService {
	
	private static ExternalModelService instance = new ExternalModelService();
	
	public static ExternalModelService getInstance() {
		return instance;
	}
	
	private Map<String, ExternalModel> models = new HashMap<String, ExternalModel>();
	private List<ModelLoader> modelLoaders = new ArrayList<>();
	
	private ExternalModelService() {
		modelLoaders.add(new AseModelLoader());
		modelLoaders.add(new ObjModelLoader());
	}
	
	public Spatial getModel(String modelId) {
		
		ExternalModel model = models.get(modelId);
		
		if (model == null) {
			for (ModelLoader modelLoader : modelLoaders) {
				if (modelLoader.canLoad(modelId)) {
					model = modelLoader.load(modelId);
					models.put(modelId, model);
				}
			}
			
		}
		Spatial result = model.getNewInstance();
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1f));
		result.addLight(al);
		
		return result;
	}
}
