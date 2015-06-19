package fr.lyrgard.hexScape.gui.desktop.view.game.rightPanel;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.view.common.chat.ChatPanel;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CardInstanceChangedOwnerMessage;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameMessagePostedMessage;
import fr.lyrgard.hexScape.message.GameObservedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.DiceService;
import fr.lyrgard.hexScape.service.MarkerService;

public class RightPanel extends JPanel {
	
	private static final long serialVersionUID = -8890928527786697273L;

	private ChatPanel chatPanel;
	
	private DiceTabbedPane dicePanel = new DiceTabbedPane();
	
	public RightPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		chatPanel = new ChatPanel(null, null);
		add(chatPanel);
		add(dicePanel);
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onPiecePlaced(PiecePlacedMessage message) {
//		String playerId = message.getPlayerId();
//		String modelId = message.getModelId();
//		
//		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
//		if (player != null) {
//			chatPanel.addPlayerAction(player, player.getDisplayName() + " added " + modelId + " to the map");
//		}
	}
	
	@Subscribe public void onPieceMoved(PieceMovedMessage message) {
//		String playerId = message.getPlayerId();
//		String pieceId = message.getPieceId();
//		
//		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
//		if (player != null) {
//			for (Player owner : Universe.getInstance().getPlayersByIds().values()) {
//				if (owner.getPiecesById().containsKey(pieceId)) {
//					PieceInstance piece = owner.getPiecesById().get(pieceId);
//					if (piece != null) {
//						chatPanel.addPlayerAction(player, player.getDisplayName() + " moved " + piece.getModelId());
//					}
//				}
//			}
//		}
	}
	
	@Subscribe public void onPieceRemoved(PieceRemovedMessage message) {
		String playerId = message.getPlayerId();
		String pieceId = message.getPieceId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
		
		if (game != null) {
			Player player = game.getPlayer(playerId);
			if (player != null) {
				for (Player owner : game.getPlayers()) {
					if (owner.getArmy() != null) {
						for (CardInstance card : player.getArmy().getCards()) {
							PieceInstance piece = card.getPiece(pieceId);
							if (piece != null) {
								chatPanel.addPlayerAction(player, player.getDisplayName() + " removed " + piece.getModelId() + " from the map");
								return;
							}
						}
					}
				}
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
		
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());

		if (game != null) {
			Player player = game.getPlayer(playerId);
			if (player != null) {
				chatPanel.addPlayerAction(player, "player " + player.getName() + " loaded army " + army.getName());
			}
		}
	}
	
	@Subscribe public void onDiceThrown(DiceThrownMessage message) {
		String diceTypeId = message.getDiceTypeId();
		List<Integer> results = message.getResults();
		String playerId = message.getPlayerId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
		
		if (game != null) {
			Player player = game.getPlayer(playerId);
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
	}
	
	@Subscribe public void onMarkerPlaced(MarkerPlacedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerTypeId = message.getMarkerTypeId();
		int number = message.getNumber();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
		MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(markerTypeId);

		if (game != null && markerDefinition != null) {
			CardInstance card = game.getCard(cardId);
			Player player = game.getPlayer(playerId);
			if (card != null && player != null) {
				CardType cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());

				if (card != null) {
					chatPanel.addPlayerAction(player, "player " + player.getDisplayName() + " added " + number + " " + markerDefinition.getName() + " to " + cardType.getName());
					return;
				}
			}
		}
	}
	
	@Subscribe public void onMarkerRemoved(MarkerRemovedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		int number = message.getNumber();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
		
		if (game != null) {
			Player player = game.getPlayer(playerId);

			if (player != null) {
				for (Player owner : game.getPlayers()) {
					if (owner.getArmy() != null) {
						CardInstance card = owner.getArmy().getCard(cardId);
						CardType cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());
						if (card != null) {
							for (MarkerInstance marker : card.getMarkers()) {
								if (marker.getId().equals(markerId)) {
									MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());

									if (markerDefinition != null) {
										chatPanel.addPlayerAction(player, "player " + player.getDisplayName() + " removed " + number + " " + markerDefinition.getName() + " from " + cardType.getName());
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	@Subscribe public void onMarkerOnCardRevealed(MarkerRevealedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardId();
		String markerId = message.getMarkerId();
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());

		if (game != null) {
			Player player = game.getPlayer(playerId);
			if (player != null) {
				for (Player owner : game.getPlayers()) {
					if (owner.getArmy() != null) {
						CardInstance card = owner.getArmy().getCard(cardId);
						CardType cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());

						if (card != null) {
							for (MarkerInstance marker : card.getMarkers()) {
								if (marker.getId().equals(markerId)) {
									MarkerDefinition markerDefinition = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());

									if (markerDefinition != null) {
										chatPanel.addPlayerAction(player, "player " + player.getDisplayName() + " revealed " + markerDefinition.getName() + " on " + cardType.getName());
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Subscribe public void onCardInstanceChangedOwner(CardInstanceChangedOwnerMessage message) {
		String cardId = message.getNewCardId();
		String oldOwnerId = message.getOldOwnerId();
		String newOwnerId = message.getNewOwnerId();
	
		
		Game game = CurrentUserInfo.getInstance().getGame();
		
		if (game != null) {
			CardInstance card = game.getCard(cardId);
			Player oldOwner = game.getPlayerByUserId(oldOwnerId);
			Player newOwner = game.getPlayerByUserId(newOwnerId);
			
			if (card != null && oldOwner != null && newOwner != null) {
				CardType cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());
						
				chatPanel.addAction("Control of " + cardType.getName() + " has passed from " + oldOwner.getDisplayName() + " to " + newOwner.getDisplayName());
			}
		}	   
	}
	
	@Subscribe public void onChatMessage(GameMessagePostedMessage message) {
		Game game = CurrentUserInfo.getInstance().getGame();
		String userId = message.getUserId();
		String messageContent = message.getMessage();
		

		User user = Universe.getInstance().getUsersByIds().get(userId);
		if (game != null && user != null) {
			Player player = game.getPlayerByUserId(userId);
			if (player != null) {
				chatPanel.addMessage(player, messageContent);
			} else {
				chatPanel.addMessage(user, messageContent);
			}
		}
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
					Game game = CurrentUserInfo.getInstance().getGame();
					if (game != null) {
						chatPanel.setGameId(gameId);
						dicePanel.setVisible(true);
					}
				}
			}
		});
	}
	
	@Subscribe public void onGameObserved(GameObservedMessage message) {
		final String gameId = message.getGameId();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
					Game game = CurrentUserInfo.getInstance().getGame();
					if (game != null) {
						chatPanel.setGameId(gameId);
						dicePanel.setVisible(false);
					}
				}
			}
		});
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		final String playerId = message.getPlayerId();
		final String gameId = message.getGameId();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (CurrentUserInfo.getInstance().getGameId() == null) {
					// The current user left the game
					chatPanel.clearText();
				} else {
					if (playerId != null && gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
						Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
						if (game != null) {
							Player player = game.getPlayer(playerId);
							chatPanel.addPlayerAction(player, player.getDisplayName() + " left the game");
						}
					}
				}
			}
		});
	}
	
	
	@Subscribe public void onGamejoined(GameJoinedMessage message) {
		final String playerId = message.getPlayerId();
		final String gameId = message.getGame().getId();

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
					Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
					if (game != null) {
						Player player = game.getPlayer(playerId);
						chatPanel.addPlayerAction(player, player.getDisplayName() + " joined the game");
					}
				}
			}
		});
	}
}
