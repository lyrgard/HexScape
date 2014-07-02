package fr.lyrgard.hexScape.server.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.message.RemoveMarkerMessage;
import fr.lyrgard.hexScape.message.RevealMarkerMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.MarkerInfo;
import fr.lyrgard.hexscape.server.network.ServerNetwork;
import fr.lyrgard.hexscape.server.network.id.IdGenerator;

public class MarkerMessageListener {

	private static MarkerMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new MarkerMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private MarkerMessageListener() {
	}
	
	
	@Subscribe public void onPlaceMarker(PlaceMarkerMessage message) {
		String playerId = message.getPlayerId();
		String gameId = message.getGameId();
		String cardId = message.getCardId();
		String markerTypeId = message.getMarkerTypeId();
		String hiddenMarkerTypeId = message.getHiddenMarkerTypeId();
		int number = message.getNumber();
		
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			String markerId = IdGenerator.getInstance().getNewMarkerId();
			MarkerInfo markerInfo = new MarkerInfo();
			markerInfo.setId(markerId);
			markerInfo.setMarkerTypeId(markerTypeId);
			markerInfo.setHiddenMarkerTypeId(hiddenMarkerTypeId);
			markerInfo.setNumber(number);
			markerInfo.setCardId(cardId);
			game.getMarkersById().put(markerInfo.getId(), markerInfo);
			
			MarkerPlacedMessage resultMessageForEmitter = new MarkerPlacedMessage(playerId, gameId, cardId, markerId, markerTypeId, hiddenMarkerTypeId, number);
			ServerNetwork.getInstance().sendMessageToPlayer(resultMessageForEmitter, playerId);
			
			// Hide the hiddenMarkerTypeId for others players
			MarkerPlacedMessage resultMessageForOthers = new MarkerPlacedMessage(playerId, gameId, cardId, markerId, markerTypeId, null, number);
			ServerNetwork.getInstance().sendMessageToGameExceptPlayer(resultMessageForOthers, gameId, playerId);
		}
	}
	
	@Subscribe public void onRemoveMarker(RemoveMarkerMessage message) {
		String playerId = message.getPlayerId();
		String gameId = message.getGameId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		int number = message.getNumber();
		
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			MarkerInfo markerInfo = game.getMarkersById().get(markerId) ;
			if (markerInfo != null) {
				if (markerInfo.getNumber() - number <= 0) {
					game.getMarkersById().remove(markerId);
				} else {
					markerInfo.setNumber(markerInfo.getNumber() - number);
				}
					
				MarkerRemovedMessage resultMessage = new MarkerRemovedMessage(playerId, gameId, cardId, markerId, number);
				ServerNetwork.getInstance().sendMessageToGame(resultMessage, gameId);
			}
		}
	}
	
	@Subscribe public void onRevealMarker(RevealMarkerMessage message) {
		String playerId = message.getPlayerId();
		String gameId = message.getGameId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null) {
			MarkerInfo markerInfo = game.getMarkersById().get(markerId) ;
			if (markerInfo != null) {
				if (markerInfo.getHiddenMarkerTypeId() != null) {
					String hiddenMarkerTypeId = markerInfo.getHiddenMarkerTypeId();
					
					markerInfo.setMarkerTypeId(hiddenMarkerTypeId);
					markerInfo.setMarkerTypeId(null);
					
					MarkerRevealedMessage resultMessage = new MarkerRevealedMessage(playerId, gameId, cardId, markerId, hiddenMarkerTypeId);
					ServerNetwork.getInstance().sendMessageToGame(resultMessage, gameId);
					
					
				}
			}
		}
	}
}
