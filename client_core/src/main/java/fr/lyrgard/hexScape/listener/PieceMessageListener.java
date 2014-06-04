package fr.lyrgard.hexScape.listener;

import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.PlacePieceMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.PieceManager;

public class PieceMessageListener {

private static PieceMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new PieceMessageListener();
			MessageBus.register(instance);
		}
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
					HexScapeCore.getInstance().getMapManager().placePiece(new PieceManager(piece));
					player.getPiecesById().put(piece.getId(), piece);
					return null;
				}
			});		
		}
		
		

	}
}
