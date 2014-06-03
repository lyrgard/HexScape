package fr.lyrgard.hexScape.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;

public class MarkerService {
	
	private static final MarkerService INSTANCE = new MarkerService();

	public static MarkerService getInstance() {
		return INSTANCE;
	}

	private static final File baseFolder = new File("asset/markers");
	private static final String markerPropertiesFilename = "marker.properties";
	private static final String markerIconFilename = "icon.png";
	
	private static final String ownerHiddenMarkerIconFilename = "ownerHiddenIcon.png";
	private static final String notOwnerHiddenMarkerIconFilename = "notOwnerHiddenIcon.png";
	
	private static final String NAME = "name";
	private static final String HIDDEN_NAME = "hiddenName";
	private static final String TYPE = "type";
	
	private MarkerService() {
		getMarkersListForCard();
	}
	
	private Map<String, MarkerDefinition> markersByIds;

	public Collection<MarkerDefinition> getMarkersListForCard() {
		if (markersByIds != null) {
			return markersByIds.values();
		}
		
		markersByIds = new TreeMap<>();
		if (baseFolder.exists()) {
			markerDefinition: for (File folder : baseFolder.listFiles()) {
				if (folder.exists() && folder.isDirectory()) {
					File markerPropertiesFile = new File(folder, markerPropertiesFilename);
					if (markerPropertiesFile.exists() && markerPropertiesFile.isFile() && markerPropertiesFile.canRead()) {
						
						Properties markerProperties = new Properties();
						InputStream input;
						try {
							input = new FileInputStream(markerPropertiesFile);
							markerProperties.load(input);
							
							MarkerDefinition marker = null;
							
							String name = markerProperties.getProperty(NAME);
							
							File iconFile = new File(folder, markerIconFilename);
							if (!iconFile.exists() || !iconFile.isFile() || !iconFile.canRead() ) {
								MessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayer().getId(), "No icon.png file was found in " + markerPropertiesFile.getAbsolutePath() + " marker definition. This marker definition will be skiped"));
								continue markerDefinition;
							}
							
							String typeString =  markerProperties.getProperty(TYPE);
							MarkerType type = null;
							try {
								type = MarkerType.valueOf(typeString);
							} catch (IllegalArgumentException e) {
								MessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayer().getId(), "The marker type \"" + typeString + "\" in " + markerPropertiesFile.getAbsolutePath() + " is not a valid type. This marker definition will be skiped"));
								continue markerDefinition;
							}
							
							switch (type) {
							case NORMAL:
							case STACKABLE:
								marker = new MarkerDefinition();
								break;
							case REVEALABLE:
								marker = new RevealableMarkerDefinition();
								((RevealableMarkerDefinition)marker).setHiddenName(markerProperties.getProperty(HIDDEN_NAME));
								File ownerHiddenMarkerIconFile = new File(folder, ownerHiddenMarkerIconFilename);
								if (ownerHiddenMarkerIconFile.exists() && ownerHiddenMarkerIconFile.isFile() && ownerHiddenMarkerIconFile.canRead() ) {
									((RevealableMarkerDefinition)marker).setOwnerHiddenMarkerImage(ownerHiddenMarkerIconFile);	
								} else {
									MessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayer().getId(), "The marker type \"" + typeString + "\" in " + markerPropertiesFile.getAbsolutePath() + " is not a valid type. This marker definition will be skiped"));
									continue markerDefinition;
								}
								File notOwnerHiddenMarkerIconFile = new File(folder, notOwnerHiddenMarkerIconFilename);
								if (notOwnerHiddenMarkerIconFile.exists() && notOwnerHiddenMarkerIconFile.isFile() && notOwnerHiddenMarkerIconFile.canRead() ) {
									((RevealableMarkerDefinition)marker).setNotOwnerHiddenMarkerImage(notOwnerHiddenMarkerIconFile);	
								} else {
									MessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayer().getId(), "No " + notOwnerHiddenMarkerIconFilename + " file was found in " + markerPropertiesFile.getAbsolutePath() + " marker definition. This marker definition will be skiped"));
									continue markerDefinition;
								}
								break;
							}
							marker.setId(folder.getName());
							marker.setType(type);
							marker.setName(name);
							marker.setImage(iconFile);
							
							
							markersByIds.put(marker.getId(), marker);
							
						} catch (IOException e) {
							MessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayer().getId(), "Error reading file \"" + markerPropertiesFile.getAbsolutePath() + "\". This marker definition will be skiped"));
						}
					}
				}
			}
		} else {
			MessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayer().getId(), "The marker definition folder \"" + baseFolder + "\" was not found"));
		}
		
		
		return markersByIds.values();
	}

	public Map<String, MarkerDefinition> getMarkersByIds() {
		return markersByIds;
	}
}
