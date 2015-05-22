package fr.lyrgard.hexScape.model.model3d.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.model.model3d.ExternalModel;
import fr.lyrgard.hexScape.model.model3d.TwoImagesExternalModel;

public class TwoImagesModelLoader extends AbstractModelLoader {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(TwoImagesModelLoader.class);
	
	private static final String HEIGHT = "height";
	private static final String WIDTH = "width";
	private static final String NAME = "name";

	@Override
	public boolean canLoad(String name) {
		TwoImagesExternalModel model = getModel(name);
		return model != null;
	}
	
	private TwoImagesExternalModel getModel(String name) {
		TwoImagesExternalModel model = null;
		for (File folder : getModelsFolders()) {
			
			File propertiesFile = new File(new File(folder, name), name + ".properties");
			File frontImage = new File(new File(folder, name), name + "-front.png");
			File sideImage = new File(new File(folder, name), name + "-side.png");
			
			if (propertiesFile.exists() && frontImage.exists() && sideImage.exists()) {
				Properties objectProperties = new Properties();
				InputStream input;
				try {
					input = new FileInputStream(propertiesFile);
					objectProperties.load(input);
					
					String heightString = objectProperties.getProperty(HEIGHT);
					String widthString = objectProperties.getProperty(WIDTH);
					String modelName = objectProperties.getProperty(NAME);
					if (modelName == null) {
						modelName = name;
					}
					
					float height = Float.parseFloat(heightString);
					float width = Float.parseFloat(widthString);
					
					model = new TwoImagesExternalModel(modelName, height, width, frontImage, sideImage);
				} catch (IOException e) {
					LOGGER.error("Error while reading 3d object definition : " + propertiesFile.getAbsolutePath(), e);
				} catch (NumberFormatException e) {
					LOGGER.error("Error while reading width or heigth from 3d object definition : " + propertiesFile.getAbsolutePath(), e);
				}
			}
		}
		return model;
	}

	@Override
	public ExternalModel load(String name) {
		TwoImagesExternalModel model = getModel(name);
		return model;
	}
}
