package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.List;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.gui.desktop.controller.chat.ChatEntryModel;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.ChatEntryModel.ChatEntryType;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CardInstanceChangedOwnerMessage;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameMessagePostedMessage;
import fr.lyrgard.hexScape.message.GameObservedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.DiceService;
import fr.lyrgard.hexScape.service.MarkerService;

public class GameChatController {

	private Game game;
	private HexScapeChatControl gameChat;
	
	
	public GameChatController(Game game, HexScapeChatControl gameChat) {
		super();
		this.game = game;
		this.gameChat = gameChat;
		
		gameChat.clear();
		
		for (Player player : game.getPlayers()) {
			if (player.getUserId() != null) {
				User user = Universe.getInstance().getUsersByIds().get(player.getUserId());
				gameChat.addUser(user);
			}
		}
	}
	
	private void displayPlayerMessage(Player player, String message) {
		gameChat.receivedChatLine(new ChatEntryModel(player, ChatEntryType.MESSAGE, message));
	}
	
	private void displayUserMessage(User user, String message) {
		gameChat.receivedChatLine(new ChatEntryModel(user, ChatEntryType.MESSAGE, message));
	}
	
	private void displayPlayerAction(Player player, String action) {
		gameChat.receivedChatLine(new ChatEntryModel(player, ChatEntryType.ACTION, action));
	}
	
	private void displayUserAction(User user, String action) {
		gameChat.receivedChatLine(new ChatEntryModel(user, ChatEntryType.ACTION, action));
	}
	
	private void displayDiceRoll(Player player, DiceType diceType, List<Integer> results) {
		gameChat.receivedChatLine(new ChatEntryModel(player, diceType, results));
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
						for (CardInstance card : owner.getArmy().getCards()) {
							PieceInstance piece = card.getPiece(pieceId);
							if (piece != null) {
								displayPlayerAction(player, "removed " + piece.getModelId() + " from the map");
								return;
							}
						}
					}
				}
			}
		}
	}
	
	@Subscribe public void onArmyLoaded(ArmyLoadedMessage message) {
		String playerId = message.getPlayerId();
		Army army = message.getArmy();
		
		
		Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());

		if (game != null) {
			Player player = game.getPlayer(playerId);
			if (player != null) {
				displayPlayerAction(player, "loaded army " + army.getName());
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
					displayPlayerAction(player, "added " + number + " " + markerDefinition.getName() + " to " + cardType.getName());
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
										displayPlayerAction(player, "removed " + number + " " + markerDefinition.getName() + " from " + cardType.getName());
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
										displayPlayerAction(player, "revealed " + markerDefinition.getName() + " on " + cardType.getName());
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
						
				displayPlayerAction(oldOwner, "Control of " + cardType.getName() + " has passed from " + oldOwner.getDisplayName() + " to " + newOwner.getDisplayName());
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
				displayPlayerMessage(player, messageContent);
			} else {
				displayUserMessage(user, messageContent);
			}
		}
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		final String userId = message.getUserId();
		final String playerId = message.getPlayerId();
		final String gameId = message.getGameId();

		if (gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			User user = Universe.getInstance().getUsersByIds().get(userId);
			if (playerId == null) {
				if (user != null) {
					displayUserAction(user, "stoped observing the game");
				}
			} else {
				Player player = game.getPlayer(playerId);
				displayPlayerAction(player, "left the game");
			}
			gameChat.removeUser(user);
		}	
	}
	
	
	@Subscribe public void onGameJoined(GameJoinedMessage message) {
		final String playerId = message.getPlayerId();
		final String gameId = message.getGame().getId();

		if (gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
			if (game != null) {
				Player player = game.getPlayer(playerId);
				displayPlayerAction(player, "joined the game");
				gameChat.addUser(Universe.getInstance().getUsersByIds().get(player.getUserId()));
			}
		}
	}
	
	@Subscribe public void onGameObserved(GameObservedMessage message) {
		final String userId = message.getUserId();
		final String gameId = message.getGameId();

		if (gameId != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			User user = Universe.getInstance().getUsersByIds().get(userId);
			if (user != null) {
				displayUserAction(user, "observe the game");
				gameChat.addUser(user);
			}
		}
	}
	
	@Subscribe public void onDiceThrown(DiceThrownMessage message) {
		String diceTypeId = message.getDiceTypeId();
		List<Integer> results = message.getResults();
		String playerId = message.getPlayerId();
		
		DiceType diceType = DiceService.getInstance().getDiceType(diceTypeId);
		Game game = CurrentUserInfo.getInstance().getGame();
		Player player = game.getPlayer(playerId);
		
		displayDiceRoll(player, diceType, results);
	}
	
}
