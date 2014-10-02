package fr.lyrgard.hexScape.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.marker.UnknownTypeMarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class MarkerService {
	
	

	public static synchronized MarkerService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MarkerService();
		}
		return INSTANCE;
	}

	private static final String MARKERS_FOLDER_NAME = "markers";
	private static final String markerPropertiesFilename = "marker.properties";
	private static final String markerIconFilename = "icon.png";
	
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String POSSIBLE_MARKERS_HIDDEN = "possibleMarkersHidden";
	private static final String CAN_BE_PLACED_REVEALED = "canBePlacedRevealed";
	
	private static MarkerService INSTANCE;
	
	private MarkerService() {
		getMarkersListForCard();
	}
	
	private Map<String, MarkerDefinition> markersByIds;
	
	private List<File> getMarkerFolders() {
		List<File> folders = new ArrayList<File>();
		File commonFolder = new File(AssetService.COMMON_ASSET_FOLDER, MARKERS_FOLDER_NAME);
		File gameFolder = new File(new File(AssetService.ASSET_FOLDER, HexScapeCore.getInstance().getGameName()), MARKERS_FOLDER_NAME);
		folders.add(commonFolder);
		folders.add(gameFolder);
		return folders;
	}

	public Collection<MarkerDefinition> getMarkersListForCard() {
		if (markersByIds != null) {
			return markersByIds.values();
		}
		
		markersByIds = new TreeMap<>();
		
		Map<String, String[]> hiddenMarkerTempCollection = new HashMap<>();
		
		for (File baseFolder : getMarkerFolders()) {
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
								String id = folder.getName();

								String name = markerProperties.getProperty(NAME);

								File iconFile = new File(folder, markerIconFilename);
								if (!iconFile.exists() || !iconFile.isFile() || !iconFile.canRead() ) {
									CoreMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getPlayerId(), "No icon.png file was found in " + markerPropertiesFile.getAbsolutePath() + " marker definition. This marker definition will be skiped"));
									continue markerDefinition;
								}

								String typeString =  markerProperties.getProperty(TYPE);
								MarkerType type = null;
								try {
									type = MarkerType.valueOf(typeString);
								} catch (IllegalArgumentException e) {
									CoreMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getPlayerId(), "The marker type \"" + typeString + "\" in " + markerPropertiesFile.getAbsolutePath() + " is not a valid type. This marker definition will be skiped"));
									continue markerDefinition;
								}

								switch (type) {
								case NORMAL:
								case STACKABLE:
									marker = new MarkerDefinition();
									break;
								case REVEALABLE:
									marker = new RevealableMarkerDefinition();
									((RevealableMarkerDefinition)marker).setCanBePlacedRevealed(Boolean.TRUE.toString().equalsIgnoreCase(markerProperties.getProperty(CAN_BE_PLACED_REVEALED, Boolean.FALSE.toString())));
									break;
								case HIDDEN:
									marker = new HiddenMarkerDefinition();
									String possibleHiddenMarkersList = markerProperties.getProperty(POSSIBLE_MARKERS_HIDDEN);
									if (possibleHiddenMarkersList != null) {
										String[] array = possibleHiddenMarkersList.split(",");
										hiddenMarkerTempCollection.put(id, array);
									}
									break;
								}
								marker.setId(id);
								marker.setType(type);
								marker.setName(name);
								marker.setImage(iconFile);


								markersByIds.put(marker.getId(), marker);

							} catch (IOException e) {
								CoreMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getPlayerId(), "Error reading file \"" + markerPropertiesFile.getAbsolutePath() + "\". This marker definition will be skiped"));
							}
						}
					}
				}
			}
		}
		
		// process HiddenMarker now that all marker definition are loaded
		for (Entry<String, String[]> entry : hiddenMarkerTempCollection.entrySet()) {
			HiddenMarkerDefinition definition = (HiddenMarkerDefinition)markersByIds.get(entry.getKey());
			for (String possibleMarkerId : entry.getValue()) {
				MarkerDefinition possibleMarkerDefinition = markersByIds.get(StringUtils.trim(possibleMarkerId));
				if (possibleMarkerDefinition != null && possibleMarkerDefinition instanceof RevealableMarkerDefinition) {
					definition.getPossibleMarkersHidden().add((RevealableMarkerDefinition)possibleMarkerDefinition);
					((RevealableMarkerDefinition)possibleMarkerDefinition).setHiddenMarkerDefinition(definition);
				}
			}
		}
		
		
		return markersByIds.values();
	}
	
	public void normalizeMarkers(Game game) {
		if (game != null) {
			for (Player player : game.getPlayers()) {
				if (player != null && player.getArmy() != null) {
					for (CardInstance card : player.getArmy().getCards()) {
						if (card != null) {
							List<MarkerInstance> newMarkers = new ArrayList<>();
							Iterator<MarkerInstance> it = card.getMarkers().iterator();
							while (it.hasNext()) {
								MarkerInstance marker = it.next();
								if (marker instanceof UnknownTypeMarkerInstance) {
									MarkerInstance newMarker = getNewMarkerInstance(marker.getMarkerDefinitionId(), marker.getId(), ((UnknownTypeMarkerInstance) marker).getNumber(), ((UnknownTypeMarkerInstance) marker).getHiddenMarkerTypeId());
									newMarkers.add(newMarker);
									it.remove();
								}
							}
							for (MarkerInstance marker : newMarkers) {
								card.addMarker(marker);
							}
						}
					}
				}
			}
		}
	}
	
	public MarkerInstance getNewMarkerInstance(String markerTypeId, String id, int number, String hiddenMarkerTypeId) {
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerTypeId);
		if (markerDefinition == null) {
			CoreMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getId(), "Unable to find marker type " + markerTypeId));
			return null;
		}
		
		MarkerInstance marker = null;
		
		switch (markerDefinition.getType()) {
		case NORMAL:
			marker = new MarkerInstance(markerDefinition.getId());
			break;
		case STACKABLE:
			marker = new StackableMarkerInstance(markerDefinition.getId(), number);
			break;
		case REVEALABLE:
			marker = new RevealableMarkerInstance(markerDefinition.getId());
			break;
		case HIDDEN:
			marker = new HiddenMarkerInstance(markerDefinition.getId(), hiddenMarkerTypeId);
			break;
		}
		marker.setId(id);
		return marker;
	}

	public Map<String, MarkerDefinition> getMarkersByIds() {
		return markersByIds;
	}
}
