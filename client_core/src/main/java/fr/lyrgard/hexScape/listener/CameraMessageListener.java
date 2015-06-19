package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.LookFreelyMessage;
import fr.lyrgard.hexScape.message.LookFromAboveMessage;
import fr.lyrgard.hexScape.message.LookFromSelectedPieceMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.service.PieceManager;

public class CameraMessageListener extends AbstractMessageListener {
	
	private static CameraMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new CameraMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private CameraMessageListener() {
	}
	
	@Subscribe
	public void onLookAtPointOfView(LookFromSelectedPieceMessage message) {
		String playerId = message.getPlayerId();
		
		if (CurrentUserInfo.getInstance().getPlayerId().equals(playerId)) {
			PieceManager pieceManager = HexScapeCore.getInstance().getHexScapeJme3Application().getSelectedPiece(); 
			if (pieceManager != null) {
				HexScapeCore.getInstance().getHexScapeJme3Application().lookThroughEyesOf(pieceManager);
			}
		}
	}
	
	@Subscribe
	public void onLookAtMap(LookFromAboveMessage message) {
		String playerId = message.getPlayerId();
		
		if (CurrentUserInfo.getInstance().getPlayerId().equals(playerId)) {
			HexScapeCore.getInstance().getHexScapeJme3Application().lookAtTheMap();
		}
	}
	
	@Subscribe
	public void onLookFreely(LookFreelyMessage message) {
		String playerId = message.getPlayerId();
		
		if (CurrentUserInfo.getInstance().getPlayerId().equals(playerId)) {
			HexScapeCore.getInstance().getHexScapeJme3Application().lookFreely();
		}
	}
	
	
}
