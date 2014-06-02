package fr.lyrgard.hexScape;


import java.awt.Color;

import com.google.common.eventbus.EventBus;

import fr.lyrgard.hexScape.listener.CardService;
import fr.lyrgard.hexScape.listener.ChatService;
import fr.lyrgard.hexScape.listener.DiceService;
import fr.lyrgard.hexScape.listener.MapService;
import fr.lyrgard.hexScape.listener.MarkerService;
import fr.lyrgard.hexScape.listener.MultiplayerService;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.ExternalModelService;

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
	
	private MultiplayerService multiplayerService;
	
	private ChatService chatService;
	
	private CardCollection cardInventory;
	
	private Player player;
	
	private HexScapeCore() {
		instance = this;
		player = new Player("Player1", Color.blue);
		hexScapeJme3Application = new HexScapeJme3Application();
	}

	public MapService getMapService() {
		return mapService;
	}

	public HexScapeJme3Application getHexScapeJme3Application() {
		return hexScapeJme3Application;
	}

	public CardCollection getCardInventory() {
		return cardInventory;
	}


	public Player getPlayer() {
		return player;
	}
	
}
