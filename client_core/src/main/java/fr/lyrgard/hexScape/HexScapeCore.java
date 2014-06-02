package fr.lyrgard.hexScape;


import java.awt.Color;

import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class HexScapeCore {

	private static HexScapeCore instance = new HexScapeCore();
	
	public static HexScapeCore getInstance() {
		return instance;
	}
	
	private HexScapeJme3Application hexScapeJme3Application;
	
	private CardCollection cardInventory;
	
	private Player player;
	
	private Game game;
	
	public Game getGame() {
		return game;
	}

	private HexScapeCore() {
		instance = this;
		player = new Player("Player1", Color.blue);
		hexScapeJme3Application = new HexScapeJme3Application();
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
