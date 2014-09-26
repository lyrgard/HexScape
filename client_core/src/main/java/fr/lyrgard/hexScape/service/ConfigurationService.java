package fr.lyrgard.hexScape.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.User;

public class ConfigurationService {
	
	private static final String CONFIG_FILENAME = "config.properties";
	
	private static final String USER_NAME_KEY = "user.name";
	//private static final String USER_COLOR_KEY = "user.color";
	private static final String SERVER_HOST_KEY = "server.host";

	
	private static final ConfigurationService INSTANCE = new ConfigurationService();
	
	public static ConfigurationService getInstance() {
		return INSTANCE;
	}
	
	private Properties properties;
	
	private ConfigurationService() {
		
		File file = new File(CONFIG_FILENAME);
		if (!file.exists()) {
			initConfig();
		} else {
			loadProperties();
		}
		
	}
	
	private void initConfig() {
		properties = new Properties();
		// set the properties value
		properties.setProperty(USER_NAME_KEY, "Player");
		properties.setProperty(SERVER_HOST_KEY, "hexscape.lyrgard.fr:4242");
			 
		save();
	}
	
	private void loadProperties() {
		properties = new Properties();
		InputStream input = null;
		try {
			 
			input = new FileInputStream(CONFIG_FILENAME);
	 
			// load a properties file
			properties.load(input);
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
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
	
	public void save() {
		OutputStream output = null;
		 
		try {
	 
			output = new FileOutputStream(CONFIG_FILENAME);
	 
			// save properties to project root folder
			properties.store(output, null);
			
			User user = CurrentUserInfo.getInstance();
			user.setName(getUserName());
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	 
		}
	}
	
	
}
