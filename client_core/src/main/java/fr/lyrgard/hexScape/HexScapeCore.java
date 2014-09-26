package fr.lyrgard.hexScape;

import fr.lyrgard.hexScape.listener.ArmyMessageListener;
import fr.lyrgard.hexScape.listener.CameraMessageListener;
import fr.lyrgard.hexScape.listener.ChatMessageListener;
import fr.lyrgard.hexScape.listener.DiceMessageListener;
import fr.lyrgard.hexScape.listener.ErrorMessageListener;
import fr.lyrgard.hexScape.listener.GameMessageListener;
import fr.lyrgard.hexScape.listener.GameRecorderListener;
import fr.lyrgard.hexScape.listener.MapMessageListener;
import fr.lyrgard.hexScape.listener.MarkerMessageListener;
import fr.lyrgard.hexScape.listener.PieceMessageListener;
import fr.lyrgard.hexScape.listener.RoomMessageListener;
import fr.lyrgard.hexScape.listener.ServerListener;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.service.ConfigurationService;
import fr.lyrgard.hexScape.service.MapManager;

public class HexScapeCore {

	private static HexScapeCore instance = new HexScapeCore();
	
	public static HexScapeCore getInstance() {
		return instance;
	}
	
	private HexScapeJme3Application hexScapeJme3Application;
	
	private boolean online = false;
	
	private CardCollection cardInventory;
	
	private MapManager mapManager;
	
	private String gameName = "heroscape";

	private HexScapeCore() {
		instance = this;
		User user = CurrentUserInfo.getInstance();
		user.setId("1");
		Universe.getInstance().getUsersByIds().put(user.getId(), user);
		
		String username = ConfigurationService.getInstance().getUserName();
		user.setName(username);
		
		hexScapeJme3Application = new HexScapeJme3Application();
		ArmyMessageListener.start();
		CameraMessageListener.start();
		ChatMessageListener.start();
		DiceMessageListener.start();
		ErrorMessageListener.start();
		GameMessageListener.start();
		GameRecorderListener.start();
		MapMessageListener.start();
		MarkerMessageListener.start();
		PieceMessageListener.start();
		RoomMessageListener.start();
		ServerListener.start();
		
	}

	public HexScapeJme3Application getHexScapeJme3Application() {
		return hexScapeJme3Application;
	}

	public CardCollection getCardInventory() {
		return cardInventory;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public void setMapManager(MapManager mapManager) {
		this.mapManager = mapManager;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
}
