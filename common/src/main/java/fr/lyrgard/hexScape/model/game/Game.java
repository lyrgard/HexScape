package fr.lyrgard.hexScape.model.game;

import java.util.ArrayList;
import java.util.Collection;
import fr.lyrgard.hexScape.model.player.Player;

public class Game {
	
	private String id;
	
	private String name;
	
	private int placesLeft;

	private Collection<Player> players;
	
	private Collection<Player> observers;

	public Collection<Player> getPlayers() {
		if (players == null) {
			players = new ArrayList<>();
		}
		return players;
	}

	public Collection<Player> getObservers() {
		if (observers == null) {
			observers = new ArrayList<>();
		}
		return observers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPlacesLeft() {
		return placesLeft;
	}

	public void setPlacesLeft(int placesLeft) {
		this.placesLeft = placesLeft;
	}

	
}
