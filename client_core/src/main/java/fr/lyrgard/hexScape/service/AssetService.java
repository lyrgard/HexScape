package fr.lyrgard.hexScape.service;

import java.io.File;
import java.util.concurrent.Callable;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.TitleScreen;

public class AssetService {

	public static final File ASSET_FOLDER = new File("asset");
	
	public static final File COMMON_ASSET_FOLDER = new File(ASSET_FOLDER, "common");  
	
	private static final AssetService INSTANCE = new AssetService();

	public static AssetService getInstance() {
		return INSTANCE;
	}

	private AssetService() {
	}

	public void importAssets(File file) {


		try {
			//create output directory is not exists
			if(!ASSET_FOLDER.exists()){
				ASSET_FOLDER.mkdir();
			}

			ZipFile zipFile = new ZipFile(file);
			zipFile.extractAll(ASSET_FOLDER.getAbsolutePath());

			TextureService.getInstance().loadTileTexture();
			ExternalModelService.getInstance().clear();
			CardService.getInstance().loadCardInventory(null);
			DiceService.getInstance().loadDiceTypes();

			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					TitleScreen.getInstance().populateNode();
					if (HexScapeCore.getInstance().getMapManager() != null) {
						HexScapeCore.getInstance().getMapManager().redraw();
					}
					return null;
				}

			});
				

		} catch (ZipException e) {
			e.printStackTrace();
			GuiMessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayerId(), "An error occurred while trying to unzip " + file.getAbsolutePath()));
		}

	}
}
