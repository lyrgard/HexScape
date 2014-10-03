package fr.lyrgard.hexScape.service;

import java.io.File;

import com.jme3.asset.AssetManager;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;

public class TextureService  {
	
	private static final String TILES_FOLDER_NAME = "tiles";
	private static final String TEXTURES_FILE_NAME = "TileTexture.bmp";
	private static TextureService INSTANCE;
	
	public static synchronized TextureService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextureService();
		}
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

		File commonFolder = new File(AssetService.COMMON_ASSET_FOLDER, TILES_FOLDER_NAME);
		File gameFolder = new File(new File(AssetService.ASSET_FOLDER, ConfigurationService.getInstance().getGameFolder()), TILES_FOLDER_NAME);
		File commonFile = new File(commonFolder, TEXTURES_FILE_NAME);
		File gameFile = new File(gameFolder, TEXTURES_FILE_NAME);
		if (gameFile.exists() && gameFile.isFile() && gameFile.canRead()) {
			tileTexture = assetManager.loadTexture(gameFile.getPath());
		} else if (commonFile.exists() && commonFile.isFile() && commonFile.canRead()) {
			tileTexture = assetManager.loadTexture(commonFile.getPath());
		} else {
			tileTexture = assetManager.loadTexture("model/texture/defaultTileTexture.bmp");
		}
	}
}
