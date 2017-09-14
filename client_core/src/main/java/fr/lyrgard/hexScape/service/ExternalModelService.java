package fr.lyrgard.hexScape.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.loader.AseModelLoader;
import fr.lyrgard.hexScape.model.model3d.loader.ModelLoader;
import fr.lyrgard.hexScape.model.model3d.loader.ObjModelLoader;
import fr.lyrgard.hexScape.model.model3d.loader.TwoImagesModelLoader;

public class ExternalModelService {
	
	private static ExternalModelService INSTANCE;
	
	public static synchronized ExternalModelService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExternalModelService();
		}
		return INSTANCE;
	}
	
	private Map<String, ExternalModel> models = new HashMap<String, ExternalModel>();
	private List<ModelLoader> modelLoaders = new ArrayList<>();
	
	private ExternalModelService() {
		modelLoaders.add(new AseModelLoader());
		modelLoaders.add(new ObjModelLoader());
		modelLoaders.add(new TwoImagesModelLoader());
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
		if (model == null) {
			GuiMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getPlayerId(), "Unable to load the 3d object \"" + modelId + "\""));
			return null;
		}
		Spatial result = model.getNewInstance();
		result.setShadowMode(ShadowMode.CastAndReceive);
		
		return result;
	}
	
	public void clear() {
		models.clear();
	}
}
