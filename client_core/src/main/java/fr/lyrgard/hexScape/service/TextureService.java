package fr.lyrgard.hexScape.service;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;

public class TextureService  {
	
	private static final TextureService INSTANCE = new TextureService();
	
	public static TextureService getInstance() {
		return INSTANCE;
	}
	
	private TextureService() {
	}
	
	private Texture tileTexture;

	public Texture getTileTexture() {
		if (tileTexture == null) {
			loadTileTexture();
		}
		return tileTexture;
	}
	
	public void loadTileTexture() {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();

		File file = new File("asset/tiles/TileTexture.bmp");
		if (file.exists() && file.isFile() && file.canRead()) {
			tileTexture = assetManager.loadTexture("asset/tiles/TileTexture.bmp");
		} else {
			tileTexture = assetManager.loadTexture("model/texture/defaultTileTexture.bmp");
		}
	}
}
