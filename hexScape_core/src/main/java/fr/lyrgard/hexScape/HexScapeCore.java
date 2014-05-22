package fr.lyrgard.hexScape;


import java.awt.Color;

import com.google.common.eventbus.EventBus;

import fr.lyrgard.hexScape.model.CardCollection;
import fr.lyrgard.hexScape.model.Player;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.DiceService;
import fr.lyrgard.hexScape.service.ExternalModelService;
import fr.lyrgard.hexScape.service.MapService;
import fr.lyrgard.hexScape.service.MarkerService;
import fr.lyrgard.hexScape.service.impl.CardServiceImpl;
import fr.lyrgard.hexScape.service.impl.DiceServiceImpl;
import fr.lyrgard.hexScape.service.impl.ExternalModelServiceImpl;
import fr.lyrgard.hexScape.service.impl.MapServiceImpl;
import fr.lyrgard.hexScape.service.impl.MarkerServiceImpl;

public class HexScapeCore {

	private static HexScapeCore instance = new HexScapeCore();
	
	public static HexScapeCore getInstance() {
		return instance;
	}
	
	private HexScapeJme3Application hexScapeJme3Application;
	
	private MapService mapService;
	
	private ExternalModelService externalModelService;
	
	private CardService cardService;
	
	private DiceService diceService;
	
	private MarkerService markerService;
	
	private CardCollection cardInventory;
	
	private Player player;
	
	private final EventBus eventBus = new EventBus();
	
	private HexScapeCore() {
		instance = this;
		mapService = new MapServiceImpl();
		externalModelService = new ExternalModelServiceImpl();
		cardService = new CardServiceImpl();
		cardInventory = cardService.loadCardInventory(null);
		diceService = new DiceServiceImpl();
		markerService = new MarkerServiceImpl();
		player = new Player("Player1", Color.blue);
		hexScapeJme3Application = new HexScapeJme3Application();
	}

	public MapService getMapService() {
		return mapService;
	}

	public HexScapeJme3Application getHexScapeJme3Application() {
		return hexScapeJme3Application;
	}

	public ExternalModelService getExternalModelService() {
		return externalModelService;
	}

	public CardService getCardService() {
		return cardService;
	}

	public CardCollection getCardInventory() {
		return cardInventory;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public Player getPlayer() {
		return player;
	}

	public DiceService getDiceService() {
		return diceService;
	}

	public MarkerService getMarkerService() {
		return markerService;
	}
	
}
