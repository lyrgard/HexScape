package fr.lyrgard.hexScape.model.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class Game {
	
	private String id;
	
	private String name;
	
	private Map map;
	
	private int playerNumber;
	
	private boolean started;

	private Collection<Player> players;
	
	private Collection<String> observersIds;

	@JsonIgnore
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Game fromJson(String string) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(string, Game.class);
	}
	
	public String toJson() throws JsonProcessingException {
		return mapper.writeValueAsString(this);
	}
	
	public Collection<Player> getPlayers() {
		if (players == null) {
			players = new ArrayList<>();
		}
		return players;
	}
	
	public Player getPlayer(String playerId) {
		for (Player player : getPlayers()) {
			if (player.getId().equals(playerId)) {
				return player;
			}
		}
		return null;
	}
	
	public Player getPlayerByUserId(String userId) {
		for (Player player : getPlayers()) {
			if (player.getUserId() != null && player.getUserId().equals(userId)) {
				return player;
			}
		}
		return null;
	}
	
	public Player getCardOwner(String cardId) {
		for (Player player : getPlayers()) {
			if (player.getArmy() != null && player.getArmy().hasCard(cardId)) {
				return player;
			}
		}
		return null;
	}
	
	public CardInstance getCard(String cardInstanceId) {
		for (Player player : getPlayers()) {
			if (player.getArmy() != null) {
				CardInstance card = player.getArmy().getCard(cardInstanceId);
				if (card != null) {
					return card;
				}
			}
		}
		return null;
	}
	
	public PieceInstance getPiece(String pieceId) {
		for (Player player : getPlayers()) {
			if (player.getArmy() != null) {
				for (CardInstance card : player.getArmy().getCards()) {
					PieceInstance piece = card.getPiece(pieceId);
					if (piece != null) {
						return piece;
					}
				}
			}
		}
		return null;
	}
	
	public Collection<Player> getFreePlayers() {
		Collection<Player> freePlayers = new ArrayList<>();
		for (Player player : players) {
			if (player.getUserId() == null) {
				freePlayers.add(player);
			}
		}
		return freePlayers;
	}

	public Collection<String> getObserversIds() {
		if (observersIds == null) {
			observersIds = new ArrayList<>();
		}
		return observersIds;
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


	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
