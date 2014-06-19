package fr.lyrgard.hexScape.server.listener;

import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexscape.server.network.ServerNetwork;

public class PieceMessageListener {

	private static PieceMessageListener INSTANCE = new PieceMessageListener();
	
	public static void start() {
		CoreMessageBus.register(INSTANCE);
	}
	
	private PieceMessageListener() {
	}
	
	
	@Subscribe public void onPiecePlaced(PiecePlacedMessage message) {
		
		final String playerId = message.getPlayerId();
		final String pieceId = message.getPieceId();
		final String cardInstanceId = message.getCardInstanceId();
		final int x = message.getX();
		final int y = message.getY();
		final int z = message.getZ();
		
		Player player = Universe.getInstance().getPlayersByIds().get(playerId);
		
		if (player != null && player.getGame() != null) {
			ServerNetwork.getInstance().sendMessageToGameExceptPlayer(message, player.getGame().getId(), playerId);
		}
				
	}
	
	@Subscribe public void onPieceMoved(PieceMovedMessage message) {
		
	}
	
	@Subscribe public void onPieceRemoved(PieceRemovedMessage message) {
		
	}
	
	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {
		
	}
	
	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		
	}
}
