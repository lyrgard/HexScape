package fr.lyrgard.hexScape.listener;

import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.message.PlacePieceMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.MapManager;
import fr.lyrgard.hexScape.service.PieceManager;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public class PieceMessageListener extends AbstractMessageListener {

	private static PieceMessageListener INSTANCE = new PieceMessageListener();
	
	public static void start() {
		CoreMessageBus.register(INSTANCE);
	}
	
	private PieceMessageListener() {
	}
	
	@Subscribe public void onPlacePieceMessage(PlacePieceMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceModelId = message.getPieceModelId();
		final String cardInstanceId = message.getCardInstanceId();
		
		final Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		if (player != null && player.getArmy() != null && player.getArmy().getCardsById().keySet().contains(cardInstanceId)) {
			final CardInstance card = player.getArmy().getCardsById().get(cardInstanceId);
			
			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {
				public Void call() throws Exception {
					String pieceId = playerId + "-" + player.getPiecesById().size();
					PieceInstance piece = new PieceInstance(pieceId, pieceModelId, card);
					HexScapeCore.getInstance().getMapManager().beginPlacingPiece(new PieceManager(piece));
					player.getPiecesById().put(piece.getId(), piece);
					return null;
				}
			});		
		}
	}
	
	@Subscribe public void onPiecePlaced(PiecePlacedMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceModelId = message.getModelId();
		final String pieceId = message.getPieceId();
		final String cardInstanceId = message.getCardInstanceId();
		final int x = message.getX();
		final int y = message.getY();
		final int z = message.getZ();
		final Direction direction = message.getDirection();
			
		if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
			// coming from ourself, advertise the placement
			if (HexScapeCore.getInstance().isOnline()) {
				ClientNetwork.getInstance().send(message);
			}
		} else {
			// coming from another player, place the piece
			Player player = Universe.getInstance().getPlayersByIds().get(playerId);
			
			if (player != null && player.getArmy() != null) {
				CardInstance cardInstance = player.getArmy().getCardsById().get(cardInstanceId);
				PieceInstance piece = new PieceInstance(pieceId, pieceModelId, cardInstance);
				piece.setDirection(direction);
				final PieceManager pieceManager = new PieceManager(piece);
				pieceManager.rotate(direction);
				player.getPiecesById().put(piece.getId(), piece);
				HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						HexScapeCore.getInstance().getMapManager().placePiece(pieceManager, x, y, z, direction);
						pieceManager.select(playerId);
						return null;
					}
					
				});
				
			}
		}
		GuiMessageBus.post(message);		
	}
	
	@Subscribe public void onPieceMoved(PieceMovedMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceId = message.getPieceId();
		final int x = message.getX();
		final int y = message.getY();
		final int z = message.getZ();
		final Direction direction = message.getDirection();
			
		if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
			// coming from ourself, advertise the placement
			if (HexScapeCore.getInstance().isOnline()) {
				ClientNetwork.getInstance().send(message);
			}
		} else {
			// coming from another player, place the piece
			final MapManager mapManager = HexScapeCore.getInstance().getMapManager();
			
			if (mapManager != null) {
				final PieceManager pieceManager = mapManager.getPieceManagersByPieceIds().get(pieceId);
				if (pieceManager != null) {
					HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							mapManager.placePiece(pieceManager, x, y, z, direction);
							return null;
						}
						
					});
				}
			}
		}	
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onPieceRemoved(final PieceRemovedMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceId = message.getPieceId();
		
		if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
			// coming from ourself, advertise the removing
			if (HexScapeCore.getInstance().isOnline()) {
				ClientNetwork.getInstance().send(message);
			}
			GuiMessageBus.post(message);
		} else {
			// coming from another player, delete the piece
			final MapManager mapManager = HexScapeCore.getInstance().getMapManager();

			if (mapManager != null) {
				final PieceManager pieceManager = mapManager.getPieceManagersByPieceIds().get(pieceId);
				if (pieceManager != null) {
					HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							mapManager.removePiece(pieceManager);
							GuiMessageBus.post(message);
							return null;
						}
					});
				}
			}			
		}
		
	}
	
	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceId = message.getPieceId();
		
		if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
			// coming from ourself, advertise the placement
			if (HexScapeCore.getInstance().isOnline()) {
				ClientNetwork.getInstance().send(message);
			}
		} else {
			// coming from another player, delete the piece
			MapManager mapManager = HexScapeCore.getInstance().getMapManager();

			if (mapManager != null) {
				final PieceManager pieceManager = mapManager.getPieceManagersByPieceIds().get(pieceId);
				if (pieceManager != null) {
					HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							pieceManager.select(playerId);
							return null;
						}
					});
				}
			}			
		}
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		final String playerId = message.getPlayerId();
		final String pieceId = message.getPieceId();
		
		if (playerId.equals(HexScapeCore.getInstance().getPlayerId())) {
			// coming from ourself, advertise the placement
			if (HexScapeCore.getInstance().isOnline()) {
				ClientNetwork.getInstance().send(message);
			}
		} else {
			// coming from another player, delete the piece
			MapManager mapManager = HexScapeCore.getInstance().getMapManager();

			if (mapManager != null) {
				final PieceManager pieceManager = mapManager.getPieceManagersByPieceIds().get(pieceId);
				if (pieceManager != null) {
					HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							pieceManager.unselect(playerId);
							return null;
						}
					});
				}
			}			
		}
		GuiMessageBus.post(message);
	}
}
