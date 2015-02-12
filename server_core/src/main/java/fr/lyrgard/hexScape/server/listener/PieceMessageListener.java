package fr.lyrgard.hexScape.server.listener;


import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSecondarySelectedMessage;
import fr.lyrgard.hexScape.message.PieceSecondaryUnselectedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
import fr.lyrgard.hexscape.server.network.ServerNetwork;

public class PieceMessageListener {

	private static PieceMessageListener INSTANCE = new PieceMessageListener();
	
	public static void start() {
		CoreMessageBus.register(INSTANCE);
	}
	
	private PieceMessageListener() {
	}
	
	
	@Subscribe public void onPiecePlaced(PiecePlacedMessage message) {
		String userId = message.getSessionUserId();
		String cardId = message.getCardInstanceId();
		String modelId = message.getModelId();
		String pieceId = message.getPieceId();
		Direction direction = message.getDirection();
		
		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user.getGame() != null && user.getPlayer() != null) {

			CardInstance card = user.getGame().getCard(cardId);
			if (card != null) {
				PieceInstance pieceInstance = new PieceInstance(pieceId, modelId, card);
				pieceInstance.setDirection(direction);
				pieceInstance.setX(x);
				pieceInstance.setY(y);
				pieceInstance.setZ(z);
				card.addPiece(pieceInstance);
				ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
				return;
			}
		}
	}
	
	@Subscribe public void onPieceMoved(PieceMovedMessage message) {
		String userId = message.getSessionUserId();
		String pieceId = message.getPieceId();
		Direction direction = message.getDirection();
		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGame() != null && user.getPlayer() != null) {
			for (Player owner : user.getGame().getPlayers()) {
				if (owner.getArmy() != null) {
					for (CardInstance card : owner.getArmy().getCards()) {
						PieceInstance piece = card.getPiece(pieceId);
						if (piece != null) {
							piece.setDirection(direction);
							piece.setX(x);
							piece.setY(y);
							piece.setZ(z);
							ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
							return;
						}
					}
				}
			}
		}
	}
	
	@Subscribe public void onPieceRemoved(PieceRemovedMessage message) {
		String userId = message.getSessionUserId();
		String pieceId = message.getPieceId();
		
		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGame() != null && user.getPlayer() != null) {
			for (Player owner : user.getGame().getPlayers()) {
				if (owner.getArmy() != null) {
					for (CardInstance card : owner.getArmy().getCards()) {
						PieceInstance piece = card.getPiece(pieceId);
						if (piece != null) {
							card.getPieces().remove(piece);
							card.getPieceLeftToPlace().add(piece.getModelId());
							ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
							return;
						}
					}
				}
			}
		}
	}
	
	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {
		String userId = message.getSessionUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGame() != null && user.getPlayer() != null) {
			ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
		}
	}
	
	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		String userId = message.getSessionUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGame() != null && user.getPlayer() != null) {
			ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
		}
	}
	
	@Subscribe public void onPieceSecondarySelected(PieceSecondarySelectedMessage message) {
		String userId = message.getSessionUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGame() != null && user.getPlayer() != null) {
			ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
		}
	}
	
	@Subscribe public void onPieceSecondaryUnselected(PieceSecondaryUnselectedMessage message) {
		String userId = message.getSessionUserId();

		User user = Universe.getInstance().getUsersByIds().get(userId);
		
		if (user != null && user.getGame() != null && user.getPlayer() != null) {
			ServerNetwork.getInstance().sendMessageToGameExceptUser(message, user.getGameId(), user.getId());
		}
	}
}
