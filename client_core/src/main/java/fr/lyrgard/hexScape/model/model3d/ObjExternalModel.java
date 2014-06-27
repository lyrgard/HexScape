package fr.lyrgard.hexScape.model.model3d;

import com.jme3.scene.Spatial;

public class ObjExternalModel implements ExternalModel {

	private Spatial spatial;
	
	private String name;
	
	@Override
	public Spatial getNewInstance() {
		return spatial.clone();
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
