package fr.lyrgard.hexScape.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.aseImport.ASEModelLoader;
import fr.lyrgard.hexScape.service.ExternalModelService;

public class ExternalModelServiceImpl implements ExternalModelService {

	private Map<String, ExternalModel> models = new HashMap<String, ExternalModel>();
	private ASEModelLoader aseModelLoader;
	
	public ExternalModelServiceImpl() {
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
