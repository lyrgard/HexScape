package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.components.chatComponent.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.components.diceComponent.DiceTabbedPane;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.DiceService;
import fr.lyrgard.hexScape.service.MarkerService;

public class RightPanel extends JPanel {
	
	private static final long serialVersionUID = -8890928527786697273L;

	private ChatPanel chatPanel;
	
	public RightPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		chatPanel = new ChatPanel(null, HexScapeCore.getInstance().getGameId());
		add(chatPanel);
		add(new DiceTabbedPane());
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onPiecePlaced(PiecePlacedMessage message) {
		String playerId = message.getPlayerId();
		String modelId = message.getModelId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			chatPanel.addPlayerAction(player, player.getName() + " added " + modelId + " to the map");
		}
	}
	
	@Subscribe public void onPieceMoved(PieceMovedMessage message) {
		String playerId = message.getPlayerId();
		String pieceId = message.getPieceId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			PieceInstance piece = player.getPiecesById().get(pieceId);
			if (piece != null) {
				chatPanel.addPlayerAction(player, player.getName() + " moved " + piece.getModelId());
			}
		}
	}
	
	@Subscribe public void onPieceRemoved(PieceRemovedMessage message) {
		String playerId = message.getPlayerId();
		String pieceId = message.getPieceId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			PieceInstance piece = player.getPiecesById().get(pieceId);
			if (piece != null) {
				chatPanel.addPlayerAction(player, player.getName() + " removed " + piece.getModelId() + " from the map");
			}
		}

	}
	
	@Subscribe public void onMapLoaded(MapLoadedMessage message) {
		Map map = message.getMap();
		chatPanel.addAction("map " + map.getName() + " has been loaded");
	}
	
	@Subscribe public void onArmyLoaded(ArmyLoadedMessage message) {
		String playerId = message.getPlayerId();
		Army army = message.getArmy();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			chatPanel.addPlayerAction(player, "player " + player.getName() + " loaded army " + army.getName());
		}
	}
	
	@Subscribe public void onDiceThrown(DiceThrownMessage message) {
		String diceTypeId = message.getDiceTypeId();
		List<Integer> results = message.getResults();
		String playerId = message.getPlayerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null) {
			DiceType diceType = DiceService.getInstance().getDiceType(diceTypeId);
			List<DiceFace> faces = new ArrayList<>();
			for (Integer result : results) {
				DiceFace face = diceType.getFaces().get(result);
				if (face != null) {
					faces.add(face);
				}
			}
			if (faces.size() == results.size()) {
				chatPanel.addDiceRoll(player, diceType, faces);
			} else {
				// TODO error
			}
		}
	}
	
	@Subscribe public void onMarkerPlaced(MarkerPlacedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		int number = message.getNumber();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerId);
		
		if (player != null && markerDefinition != null) {
			CardInstance card = player.getArmy().getCardsById().get(cardId);
			CardType cardType = CardService.getInstance().getCardInventory().getCardsById().get(card.getCardTypeId());
			
			if (card != null) {
				chatPanel.addPlayerAction(player, "player " + player.getName() + " added " + number + " " + markerDefinition.getName() + " to " + cardType.getName());
			}
		}
	}
	
	@Subscribe public void onMarkerRemoved(MarkerRemovedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		int number = message.getNumber();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null) {
			CardInstance card = player.getArmy().getCardsById().get(cardId);
			CardType cardType = CardService.getInstance().getCardInventory().getCardsById().get(card.getCardTypeId());
			if (card != null) {
				MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerId);

				if (markerDefinition != null) {
					chatPanel.addPlayerAction(player, "player " + player.getName() + " removed " + number + " " + markerDefinition.getName() + " from " + cardType.getName());
				}
			}
		}
	}
	
	
	@Subscribe public void onMarkerOnCardRevealed(MarkerRevealedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerId);
		
		if (player != null && markerDefinition != null) {
			CardInstance card = player.getArmy().getCardsById().get(cardId);
			CardType cardType = CardService.getInstance().getCardInventory().getCardsById().get(card.getCardTypeId());
			
			if (card != null) {
				chatPanel.addPlayerAction(player, "player " + player.getName() + " revealed " + markerDefinition.getName() + " on " + cardType.getName());
			}
		}
	}
	
	@Subscribe public void onChatMessage(MessagePostedMessage message) {
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		String messageContent = message.getMessage();
		
		if (HexScapeCore.getInstance().getGameId().equals(gameId)) {
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);
			if (player != null) {
				chatPanel.addMessage(player, messageContent);
			}
		}
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		String gameId = message.getGameId();
		chatPanel.setGameId(gameId);
	}
}
