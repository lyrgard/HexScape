package fr.lyrgard.hexScape;


import java.awt.Color;

import fr.lyrgard.hexScape.listener.ArmyMessageListener;
import fr.lyrgard.hexScape.listener.ChatMessageLocalListener;
import fr.lyrgard.hexScape.listener.DiceMessageLocalListener;
import fr.lyrgard.hexScape.listener.MapMessageListener;
import fr.lyrgard.hexScape.listener.MarkerMessageLocalListener;
import fr.lyrgard.hexScape.listener.PieceMessageListener;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.MapManager;

public class HexScapeCore {

	private static HexScapeCore instance = new HexScapeCore();
	
	public static HexScapeCore getInstance() {
		return instance;
	}
	
	private HexScapeJme3Application hexScapeJme3Application;
	
	private CardCollection cardInventory;
	
	private String playerId = "1";
	
	private String roomId = "NO_ROOM";
	
	private String gameId = "NO_GAME";
	
	private MapManager mapManager;

	private HexScapeCore() {
		instance = this;
		Universe.getInstance().getPlayersByIds().put(playerId, new Player("Player1", ColorEnum.BLUE));
		hexScapeJme3Application = new HexScapeJme3Application();
		ArmyMessageListener.start();
		ChatMessageLocalListener.start();
		DiceMessageLocalListener.start();
		MapMessageListener.start();
		MarkerMessageLocalListener.start();
		PieceMessageListener.start();
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
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public void setMapManager(MapManager mapManager) {
		this.mapManager = mapManager;
	}
	
}
