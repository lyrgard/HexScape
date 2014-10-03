package fr.lyrgard.hexScape.listener;

import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.message.RemoveMarkerMessage;
import fr.lyrgard.hexScape.message.RevealMarkerMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerType;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.MarkerService;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public class MarkerMessageListener extends AbstractMessageListener {

	private static MarkerMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new MarkerMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	public static void stop() {
		if (instance != null) {
			CoreMessageBus.unregister(instance);
			instance = null;
		}
	}
	
	private MarkerMessageListener() {
	}
	
	@Subscribe public void onPlaceMarkerMessage(PlaceMarkerMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String cardId = message.getCardId();
			String markerId = UUID.randomUUID().toString();
			int number = message.getNumber();
			String markerTypeId = message.getMarkerTypeId();
			String hiddenMarkerTypeId = message.getHiddenMarkerTypeId();
			MarkerPlacedMessage resultMessage = new MarkerPlacedMessage(CurrentUserInfo.getInstance().getPlayerId(), cardId, markerId, markerTypeId, hiddenMarkerTypeId, number);
			CoreMessageBus.post(resultMessage);
		}
		
	}
	
	@Subscribe public void onMarkerPlaced(MarkerPlacedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		String markerTypeId = message.getMarkerTypeId();
		int number = message.getNumber();
		String hiddenMarkerTypeId = message.getHiddenMarkerTypeId();
		
		String gameId = CurrentUserInfo.getInstance().getGameId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find game " + gameId));
			return;
		} 
		
		Player player = game.getPlayer(playerId);
		if (player == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find player " + playerId));
			return;
		} 
		
		Army army = player.getArmy();
		if (army == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find army for player " + playerId));
			return;
		}
		
		CardInstance card = army.getCard(cardId);
		if (card == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find card " + cardId + " for player " + playerId + " in game " + gameId));
			return;
		}
		
		
		
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerTypeId);
		if (markerDefinition == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find marker type " + markerTypeId));
			return;
		}
		
		MarkerInstance marker = MarkerService.getInstance().getNewMarkerInstance(markerTypeId, markerId, number, hiddenMarkerTypeId);
		if (marker != null) {
			card.addMarker(marker);
		}
		
		GuiMessageBus.post(message);
	}

	@Subscribe public void onRemoveMarkerMessage(RemoveMarkerMessage message) {
		
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			String cardId = message.getCardId();
			String markerId = message.getMarkerId();
			int number = message.getNumber();
			MarkerRemovedMessage resultMessage = new MarkerRemovedMessage(CurrentUserInfo.getInstance().getPlayerId(), cardId, markerId, number);
			CoreMessageBus.post(resultMessage);
		}
	}
	
	@Subscribe public void onMarkerRemoved(MarkerRemovedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		int number = message.getNumber();
		
		String gameId = CurrentUserInfo.getInstance().getGameId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find game " + gameId));
			return;
		} 
		
		Player player = game.getPlayer(playerId);
		if (player == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find player " + playerId));
			return;
		} 
		
		Army army = player.getArmy();
		if (army == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find army for player " + playerId));
			return;
		}
		
		CardInstance card = army.getCard(cardId);
		if (card == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find card " + cardId + " for player " + playerId + " in game " + gameId));
			return;
		}
		

		for (MarkerInstance marker : card.getMarkers()) {
			if (marker.getId().equals(markerId)) {

				MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());
				if (markerDefinition == null) {
					CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find marker type " + markerId));
					return;
				}

				if (markerDefinition.getType() == MarkerType.STACKABLE) {
					int currentNumber = ((StackableMarkerInstance)marker).getNumber();
					if (currentNumber <= number) {
						card.getMarkers().remove(marker);
					} else {
						((StackableMarkerInstance)marker).setNumber(currentNumber - number);
					}
				} else {
					card.getMarkers().remove(marker);
				}
				break;
			}
		}
		GuiMessageBus.post(message);
	}

	@Subscribe public void onRevealMarkerMessage(RevealMarkerMessage message) {
		
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			
			String cardId = message.getCardId();
			String markerId = message.getMarkerId();
			
			String gameId = CurrentUserInfo.getInstance().getGameId();
			String playerId = CurrentUserInfo.getInstance().getPlayerId();
			
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			if (game == null) {
				CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find game " + gameId));
				return;
			} 
			
			Player player = game.getPlayer(playerId);
			if (player == null) {
				CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find player " + playerId));
				return;
			} 
			
			Army army = player.getArmy();
			if (army == null) {
				CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find army for player " + playerId));
				return;
			}
			
			CardInstance card = army.getCard(cardId);
			if (card == null) {
				CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find card " + cardId + " for player " + playerId + " in game " + gameId));
				return;
			}
			
			for (MarkerInstance marker : card.getMarkers()) {
				if (marker.getId().equals(markerId)) {
					if (!(marker instanceof HiddenMarkerInstance)) {
						return;
					}
					String hiddenMarkerTypeId = ((HiddenMarkerInstance)marker).getHiddenMarkerDefinitionId();
					MarkerRevealedMessage resultMessage = new MarkerRevealedMessage(playerId, cardId, markerId, hiddenMarkerTypeId);
					CoreMessageBus.post(resultMessage);
				}
			}
		}
	}
	
	@Subscribe public void onMarkerRevealed(MarkerRevealedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		String hiddenMarkerTypeId = message.getHiddenMarkerTypeId();
		
		String gameId = CurrentUserInfo.getInstance().getGameId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find game " + gameId));
			return;
		} 
		
		CardInstance card = game.getCard(cardId);
		if (card == null) {
			CoreMessageBus.post(new ErrorMessage(playerId, "Unable to find card " + cardId + " in game " + gameId));
			return;
		}
		
		for (MarkerInstance marker : card.getMarkers()) {
			if (marker.getId().equals(markerId)) {
				if (!(marker instanceof HiddenMarkerInstance)) {
					return;
				}
				
				RevealableMarkerInstance revealedMarker = new RevealableMarkerInstance(hiddenMarkerTypeId);
				revealedMarker.setId(markerId);
				card.getMarkers().remove(marker);
				card.addMarker(revealedMarker);
				
				GuiMessageBus.post(message);
				break;
			}
		}
	}

}
