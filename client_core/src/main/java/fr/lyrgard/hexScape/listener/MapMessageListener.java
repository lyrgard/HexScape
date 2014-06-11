package fr.lyrgard.hexScape.listener;

import java.io.File;
import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.LoadMapMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.model.Scene;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.service.MapManager;

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
		final String playerId = message.getPlayerId();
		final File file = message.getMapFile();
		
		MapManager mapManager = MapManager.fromFile(file);
						
		CoreMessageBus.post(new MapLoadedMessage(playerId, mapManager.getMap()));
	}
	
	@Subscribe public void onMapLoaded(MapLoadedMessage message) {
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onDisplayMap(DisplayMapMessage message) {
		Map map = message.getMap();
		final MapManager mapManager = new MapManager(map);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeCore.getInstance().getHexScapeJme3Application().setScene(null);
				
				Scene scene = new Scene();
				scene.setMapManager(mapManager);
				HexScapeCore.getInstance().getHexScapeJme3Application().setScene(scene);
				
				HexScapeCore.getInstance().setMapManager(mapManager);
			
				return null;
			}
		});		
	}
}
