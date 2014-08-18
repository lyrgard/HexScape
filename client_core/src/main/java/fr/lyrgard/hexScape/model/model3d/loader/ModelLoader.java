package fr.lyrgard.hexScape.model.model3d.loader;

import fr.lyrgard.hexScape.model.model3d.ExternalModel;

public interface ModelLoader {

	public boolean canLoad(String name);
	
	public ExternalModel load(String name);
}
