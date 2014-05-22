package fr.lyrgard.hexScape.service;

import com.jme3.scene.Spatial;

public interface ExternalModelService {
	Spatial getModel(String name);
}
