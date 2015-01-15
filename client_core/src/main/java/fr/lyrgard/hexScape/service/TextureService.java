package fr.lyrgard.hexScape.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;

public class TextureService  {
	
	private static final String TILES_FOLDER_NAME = "tiles";
	private static final String TEXTURES_FILE_NAME = "TileTexture.bmp";
	private static TextureService INSTANCE;
	private static int number;
	
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
		BufferedImage bimg = null;
		try {
			if (gameFile.exists() && gameFile.isFile() && gameFile.canRead()) {
				tileTexture = assetManager.loadTexture(gameFile.getPath());
				bimg = ImageIO.read(new File(gameFile.getPath()));
			} else if (commonFile.exists() && commonFile.isFile() && commonFile.canRead()) {
				tileTexture = assetManager.loadTexture(commonFile.getPath());
				bimg = ImageIO.read(new File(gameFile.getPath()));
			} else {
				String resourceLocation = "model/texture/defaultTileTexture.bmp";
				tileTexture = assetManager.loadTexture(resourceLocation);
				bimg = ImageIO.read(ClassLoader.getSystemResourceAsStream(resourceLocation)); 
			}
			
			int width          = bimg.getWidth();
			int height         = bimg.getHeight();
			
			number = width / height;
		} catch (IOException e) {
			throw new RuntimeException("unable to load texture", e);
		}
	}
	
	public Vector2f getCoord(int texture, float x, float y) {
		if (number != 0) {
			float offset = (texture % number)  / (float)number;
			float newX = x / number + offset;
			float newY = y;
			return new Vector2f(newX, newY);
		} else {
			return new Vector2f(0,0);
		}
	}
}
