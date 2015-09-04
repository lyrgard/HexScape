package fr.lyrgard.hexScape.listener;

import java.io.File;
import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DisplayGameMessage;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.LoadMapMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.MapManager;
import fr.lyrgard.hexScape.service.PieceManager;

public class MapMessageListener extends AbstractMessageListener {
	
	private static MapMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new MapMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private MapMessageListener() {
	}


	@Subscribe public void onLoadMapMessage(LoadMapMessage message) {
		final File file = message.getMapFile();
		
		final MapManager mapManager = MapManager.fromFile(file);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				//HexScapeCore.getInstance().getHexScapeJme3Application().setScene(mapManager);
				
				if (mapManager != null) {
					CoreMessageBus.post(new MapLoadedMessage(CurrentUserInfo.getInstance().getId(), mapManager.getMap()));
				}
				return null;
			}
		});
	}
	
	@Subscribe public void onMapLoaded(MapLoadedMessage message) {
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onDisplayMap(DisplayMapMessage message) {
		Map map = message.getMap();
		
		if (map != null) {

			final MapManager mapManager = new MapManager(map);

			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

				public Void call() throws Exception {
					HexScapeCore.getInstance().getHexScapeJme3Application().setScene(mapManager);
					return null;
				}
			});		
		}
	}
	
	
	@Subscribe public void onDisplayGame(DisplayGameMessage message) {
		String gameId = message.getGameId();
		final boolean displayFigures = message.isDisplayFigures();

		final Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null && game.getMap() != null) {

			final MapManager mapManager = new MapManager(game.getMap());

			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

				public Void call() throws Exception {
					HexScapeCore.getInstance().getHexScapeJme3Application().setScene(mapManager);

					
					if (displayFigures) {
						for (Player player : game.getPlayers()) {
							if (player.getArmy() != null) {
								for (CardInstance card : player.getArmy().getCards()) {
									for (PieceInstance piece : card.getPieces()) {
										final PieceManager pieceManager = new PieceManager(piece);
										pieceManager.rotate(piece.getDirection());
										mapManager.placePiece(pieceManager, piece.getX(), piece.getY(), piece.getZ(), piece.getDirection());
									}
								}
							}
						}
					}

					return null;
				}
			});		
		}
	}
}
