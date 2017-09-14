package fr.lyrgard.hexScape.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.player.User;

public class ConfigurationService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);
	
	public static final String DEFAULT_GAME_NAME = "DEFAULT_GAME_NAME";
	
	private static final File CONFIG_FILE = new File(HexScapeCore.APP_DATA_FOLDER, "config.properties");
	
	private static final String USER_NAME_KEY = "user.name";
	//private static final String USER_COLOR_KEY = "user.color";
	private static final String SERVER_HOST_KEY = "server.host";
	
	private static final String GAME_FOLDER = "game.folder";
	
	private static final String LOCATION_SAVED_GAMES = "location.saved_games";
	private static final String LOCATION_MAPS = "location.maps";
	private static final String LOCATION_ARMIES = "location.armies";
	
	private static final String SETTINGS_LIGHT_SUN_INTENSITY = "settings.light.sun.intensity";
	private static final String SETTINGS_LIGHT_AMBIANT_INTENSITY = "settings.light.ambiant.intensity";
	private static final String SETTINGS_LIGHT_SUN_HEIGHT = "settings.light.sun.height";
	private static final String SETTINGS_LIGHT_SUN_POSITION = "settings.light.sun.position";
	
	private static ConfigurationService INSTANCE;
	
	public static synchronized ConfigurationService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConfigurationService();
		}
		return INSTANCE;
	}
	
	private Properties properties;
	
	private ConfigurationService() {
		
		if (!CONFIG_FILE.exists()) {
			initConfig();
		} else {
			loadProperties();
		}
		
	}
	
	private void initConfig() {
		if (!HexScapeCore.APP_DATA_FOLDER.exists()) {
			HexScapeCore.APP_DATA_FOLDER.mkdir();
		}
		
		properties = new Properties();
		// set the properties value
		setUserName("Player");
		setServerHost("hexscape.lyrgard.fr:4242");
		
		initGameFolder();
		
		save();
	}
	
	public List<String> getGameFolders() {
		List<String> gameFolders = new ArrayList<>();
		File assetFolder = AssetService.ASSET_FOLDER;
		if (assetFolder.exists() && assetFolder.isDirectory()) {
			for (File file : assetFolder.listFiles()) {
				if (file.isDirectory() && !file.getName().equals(AssetService.COMMON_ASSET_FOLDER.getName())) {
					gameFolders.add(file.getName());
				}
			}
		}
		return gameFolders;
	}
	
	private void loadProperties() {
		properties = new Properties();
		InputStream input = null;
		try {
			 
			input = new FileInputStream(CONFIG_FILE.getAbsolutePath());
	 
			// load a properties file
			properties.load(input);
	 
		} catch (IOException e) {
			LOGGER.error("Error while loading config : " + CONFIG_FILE.getAbsolutePath(), e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("Error while closing config after loading : " + CONFIG_FILE.getAbsolutePath(), e);
				}
			}
		}
	}
	
	
	
	public String getUserName() {
		return properties.getProperty(USER_NAME_KEY);
	}
	
	public void setUserName(String name) {
		properties.put(USER_NAME_KEY, name);
	}
	
	public String getServerHost() {
		return properties.getProperty(SERVER_HOST_KEY);
	}
	
	public void setServerHost(String host) {
		properties.put(SERVER_HOST_KEY, host);
	}
	
	public String getGameFolder() {
		String gameFolder = properties.getProperty(GAME_FOLDER);
		if (gameFolder == null) {
			gameFolder = DEFAULT_GAME_NAME;
		}
		return gameFolder;
	}
	
	public void setGameFolder(String gameFolder) {
		properties.put(GAME_FOLDER, gameFolder);
	}
	
	public void initGameFolder() {
		List<String> gameFolders = getGameFolders();
		if (!gameFolders.isEmpty()) {
			setGameFolder(gameFolders.get(0));
		}
	}
	
	public String getLocationSavedGames() {
		return properties.getProperty(LOCATION_SAVED_GAMES);
	}
	
	public void setLocationSavedGames(File location) {
		properties.put(LOCATION_SAVED_GAMES, location.getAbsolutePath());
	}
	
	public String getLocationMaps() { 
		return properties.getProperty(LOCATION_MAPS);
	}
	
	public void setLocationMaps(File location) {
		properties.put(LOCATION_MAPS, location.getAbsolutePath());
	}
	
	public String getLocationArmies() {
		return properties.getProperty(LOCATION_ARMIES);
	}
	
	public void setLocationArmies(File location) {
		properties.put(LOCATION_ARMIES, location.getAbsolutePath());
	}
	
	public int getSunLightIntensity() {
		return Integer.parseInt(properties.getProperty(SETTINGS_LIGHT_SUN_INTENSITY, "8"));
	}
	
	public void setSunLightIntensity(int sunLightIntensity) {
		properties.put(SETTINGS_LIGHT_SUN_INTENSITY, Integer.toString(sunLightIntensity));
	}
	
	public int getSunHeight() {
		return Integer.parseInt(properties.getProperty(SETTINGS_LIGHT_SUN_HEIGHT, "50"));
	}
	
	public void setSunHeight(int sunHeight) {
		properties.put(SETTINGS_LIGHT_SUN_HEIGHT, Integer.toString(sunHeight));
	}
	
	public int getSunPosition() {
		return Integer.parseInt(properties.getProperty(SETTINGS_LIGHT_SUN_POSITION, "50"));
	}
	
	public void setSunPosition(int sunPosition) {
		properties.put(SETTINGS_LIGHT_SUN_POSITION, Integer.toString(sunPosition));
	}
	
	public int getAmbiantLightIntensity() {
		return Integer.parseInt(properties.getProperty(SETTINGS_LIGHT_AMBIANT_INTENSITY, "4"));
	}
	
	public void setAmbiantLightIntensity(int ambiantLightIntensity) {
		properties.put(SETTINGS_LIGHT_AMBIANT_INTENSITY, Integer.toString(ambiantLightIntensity));
	}
	
	public void save() {
		OutputStream output = null;
		 
		try {
	 
			output = new FileOutputStream(CONFIG_FILE.getAbsolutePath());
	 
			// save properties to project root folder
			properties.store(output, null);
			
			User user = CurrentUserInfo.getInstance();
			user.setName(getUserName());
	 
		} catch (IOException e) {
			LOGGER.error("Error while writing config : " + CONFIG_FILE.getAbsolutePath(), e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					LOGGER.error("Error while closing config after writing : " + CONFIG_FILE.getAbsolutePath(), e);
				}
			}	 
		}
	}
	
	
}
