package fr.lyrgard.hexScape.service;

import java.util.HashMap;
import java.util.Map;

import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.aseImport.ASEModelLoader;

public class ExternalModelService {
	
	private static ExternalModelService instance = new ExternalModelService();
	
	public static ExternalModelService getInstance() {
		return instance;
	}
	
	private Map<String, ExternalModel> models = new HashMap<String, ExternalModel>();
	private ASEModelLoader aseModelLoader;
	
	private ExternalModelService() {
		aseModelLoader = new ASEModelLoader();
	}
	
	public Spatial getModel(String name) {
		
		ExternalModel model = models.get(name);
		
		if (model == null) {
			model = aseModelLoader.load(name);
			models.put(name, model);
		}
		Spatial result = model.getNewInstance();
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.5f));
		result.addLight(al);
		
		return result;
	}
}
