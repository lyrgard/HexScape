package fr.lyrgard.hexScape.model;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

import fr.lyrgard.hexScape.HexScapeCore;

public class Sky {

private static final Sky INSTANCE = new Sky();
	
	public static Sky getInstance() {
		return INSTANCE;
	}
	
	private Sky() {	
	}
	
	private Spatial spatial;
	
	private void loadSky() {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Texture west = assetManager.loadTexture("model/texture/sky/left.png");
        Texture east = assetManager.loadTexture("model/texture/sky/right.png");
        Texture north = assetManager.loadTexture("model/texture/sky/front.png");
        Texture south = assetManager.loadTexture("model/texture/sky/back.png");
        Texture up = assetManager.loadTexture("model/texture/sky/top.png");
        Texture down = assetManager.loadTexture("model/texture/sky/bottom.png");

		spatial = SkyFactory.createSky(HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager(), west, east, north, south, up, down);
		spatial.setShadowMode(ShadowMode.Off);
	}

	public Spatial getSpatial() {
		if (spatial == null) {
			loadSky();
		}
		return spatial;
	}
}
