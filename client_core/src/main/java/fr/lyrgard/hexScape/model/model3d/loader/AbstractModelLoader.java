package fr.lyrgard.hexScape.model.model3d.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.service.AssetService;

public abstract class AbstractModelLoader implements ModelLoader {

	private static final String MODELS_FOLDER_NAME = "3dObjects";
	
	protected List<File> getModelsFolders() {
		List<File> folders = new ArrayList<File>();
		File commonFolder = new File(AssetService.COMMON_ASSET_FOLDER, MODELS_FOLDER_NAME);
		File gameFolder = new File(new File(AssetService.ASSET_FOLDER, HexScapeCore.getInstance().getGameName()), MODELS_FOLDER_NAME);
		folders.add(commonFolder);
		folders.add(gameFolder);
		return folders;
	}
}
