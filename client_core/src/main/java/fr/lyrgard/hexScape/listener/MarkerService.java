package fr.lyrgard.hexScape.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ErrorEvent;
import fr.lyrgard.hexScape.event.marker.MarkerRevealedOnCardEvent;
import fr.lyrgard.hexScape.event.marker.MarkersOnCardChangedEvent;
import fr.lyrgard.hexScape.model.card.Card;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;

public class MarkerService {

	private static final File baseFolder = new File("asset/markers");
	private static final String markerPropertiesFilename = "marker.properties";
	private static final String markerIconFilename = "icon.png";
	
	private static final String ownerHiddenMarkerIconFilename = "ownerHiddenIcon.png";
	private static final String notOwnerHiddenMarkerIconFilename = "notOwnerHiddenIcon.png";
	
	
	private static final String NAME = "name";
	private static final String HIDDEN_NAME = "hiddenName";
	private static final String TYPE = "type";
	
	private Map<String, MarkerDefinition> markersForCards;

	public Collection<MarkerDefinition> getMarkersListForCard() {
		if (markersForCards != null) {
			return markersForCards.values();
		}
		
		markersForCards = new TreeMap<>();
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
								HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("No icon.png file was found in " + markerPropertiesFile.getAbsolutePath() + " marker definition. This marker definition will be skiped"));
								continue markerDefinition;
							}
							
							String typeString =  markerProperties.getProperty(TYPE);
							MarkerType type = null;
							try {
								type = MarkerType.valueOf(typeString);
							} catch (IllegalArgumentException e) {
								HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("The marker type \"" + typeString + "\" in " + markerPropertiesFile.getAbsolutePath() + " is not a valid type. This marker definition will be skiped"));
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
									HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("No " + ownerHiddenMarkerIconFilename + " file was found in " + markerPropertiesFile.getAbsolutePath() + " marker definition. This marker definition will be skiped"));
									continue markerDefinition;
								}
								File notOwnerHiddenMarkerIconFile = new File(folder, notOwnerHiddenMarkerIconFilename);
								if (notOwnerHiddenMarkerIconFile.exists() && notOwnerHiddenMarkerIconFile.isFile() && notOwnerHiddenMarkerIconFile.canRead() ) {
									((RevealableMarkerDefinition)marker).setNotOwnerHiddenMarkerImage(notOwnerHiddenMarkerIconFile);	
								} else {
									HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("No " + notOwnerHiddenMarkerIconFilename + " file was found in " + markerPropertiesFile.getAbsolutePath() + " marker definition. This marker definition will be skiped"));
									continue markerDefinition;
								}
								break;
							}
							marker.setId(folder.getName());
							marker.setType(type);
							marker.setName(name);
							marker.setImage(iconFile);
							
							
							markersForCards.put(marker.getId(), marker);
							
						} catch (IOException e) {
							HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("Error reading file \"" + markerPropertiesFile.getAbsolutePath() + "\". This marker definition will be skiped"));
						}
					}
				}
			}
		} else {
			HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("The marker definition folder \"" + baseFolder + "\" was not found"));
		}
		
		
		return markersForCards.values();
	}

	public void addMarkerToCard(Card card, String id) {
		int number = 1; 
		
		if (markersForCards == null) {
			getMarkersListForCard();
		}
		MarkerDefinition markerDefinition = markersForCards.get(id);
		
		MarkerInstance marker = null;
		
		switch (markerDefinition.getType()) {
		case NORMAL:
			marker = new MarkerInstance();
			break;
		case STACKABLE:
			addStackableMarkerToCard(card, id, number);
			return;
		case REVEALABLE:
			marker = new RevealableMarkerInstance();
			((RevealableMarkerInstance)marker).setHidden(true);
			break;
		}
		marker.setMarkerDefinition(markerDefinition);
		card.getMarkers().add(marker);
		
		HexScapeCore.getInstance().getEventBus().post(new MarkersOnCardChangedEvent(HexScapeCore.getInstance().getPlayer(), card, marker, number));
	}

	public void addStackableMarkerToCard(Card card, String id, int number) {
		if (markersForCards == null) {
			getMarkersListForCard();
		}
		MarkerDefinition markerDefinition = markersForCards.get(id);
		
		if (markerDefinition.getType() != MarkerType.STACKABLE) {
			return;
		}
		
		for (MarkerInstance markerOnCard : card.getMarkers()) {
			if (markerOnCard.getMarkerDefinition() == markerDefinition) {
				// a marker of this type is already on the card. Add "number" to it
				((StackableMarkerInstance)markerOnCard).setNumber(((StackableMarkerInstance)markerOnCard).getNumber() + number);
				HexScapeCore.getInstance().getEventBus().post(new MarkersOnCardChangedEvent(HexScapeCore.getInstance().getPlayer(), card, markerOnCard, number));
				return;
			}
		}
		StackableMarkerInstance marker = new StackableMarkerInstance();
		marker.setMarkerDefinition(markerDefinition);
		marker.setNumber(number);
		card.getMarkers().add(marker);
		HexScapeCore.getInstance().getEventBus().post(new MarkersOnCardChangedEvent(HexScapeCore.getInstance().getPlayer(), card, marker, number));
	}

	public void removeMarkerFromCard(Card card, MarkerInstance marker) {
		if (card.getMarkers().contains(marker)) {
			card.getMarkers().remove(marker);
			int number = -1;
			if (marker.getMarkerDefinition().getType() == MarkerType.STACKABLE) {
				number = -((StackableMarkerInstance)marker).getNumber();
			}
			HexScapeCore.getInstance().getEventBus().post(new MarkersOnCardChangedEvent(HexScapeCore.getInstance().getPlayer(), card, marker, number));
		}
		
	}


	public void removeAllMarkersFromCard(Card card) {
		if (!card.getMarkers().isEmpty()) {
			card.getMarkers().clear();
			HexScapeCore.getInstance().getEventBus().post(new MarkersOnCardChangedEvent(HexScapeCore.getInstance().getPlayer(), card, null, 0));
		}
	}

	public void revealMarkerOnCard(Card card, RevealableMarkerInstance marker) {
		if (card.getMarkers().contains(marker)) {
			marker.setHidden(false);
			HexScapeCore.getInstance().getEventBus().post(new MarkerRevealedOnCardEvent(HexScapeCore.getInstance().getPlayer(), card, marker, 0));
		}
	}

}
