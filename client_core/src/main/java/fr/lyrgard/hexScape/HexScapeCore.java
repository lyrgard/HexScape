package fr.lyrgard.hexScape;

import fr.lyrgard.hexScape.listener.ArmyMessageListener;
import fr.lyrgard.hexScape.listener.ChatMessageListener;
import fr.lyrgard.hexScape.listener.DiceMessageListener;
import fr.lyrgard.hexScape.listener.ErrorMessageListener;
import fr.lyrgard.hexScape.listener.GameMessageListener;
import fr.lyrgard.hexScape.listener.MapMessageListener;
import fr.lyrgard.hexScape.listener.MarkerMessageListener;
import fr.lyrgard.hexScape.listener.PieceMessageListener;
import fr.lyrgard.hexScape.listener.RoomMessageListener;
import fr.lyrgard.hexScape.listener.ServerListener;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
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
	
	private String playerId = "1";
	
	private MapManager mapManager;

	private HexScapeCore() {
		instance = this;
		String username = ConfigurationService.getInstance().getUserName();
		Universe.getInstance().getPlayersByIds().put(playerId, new Player(username, ColorEnum.RED));
		hexScapeJme3Application = new HexScapeJme3Application();
		ArmyMessageListener.start();
		ChatMessageListener.start();
		DiceMessageListener.start();
		ErrorMessageListener.start();
		GameMessageListener.start();
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

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getGameId() {
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null && player.getGame() != null) {
			return player.getGame().getId();
		} else {
			return "";
		}
	}


	public String getRoomId() {
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null && player.getRoom() != null) {
			return player.getRoom().getId();
		} else {
			return "";
		}
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
	
}
