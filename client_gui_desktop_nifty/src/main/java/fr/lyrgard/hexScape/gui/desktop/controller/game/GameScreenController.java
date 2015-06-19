package fr.lyrgard.hexScape.gui.desktop.controller.game;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatTextSendEvent;
import fr.lyrgard.hexScape.message.PostGameMessageMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.game.Game;

public class GameScreenController implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private Game game;
	private ArmiesPanelController armiesPanelController;
	private GameChatController gameChatController;
	private CameraButtonController cameraButtonController;
	private DicePanelController dicePanelController;
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onStartScreen() {
		game = CurrentUserInfo.getInstance().getGame();
		HexScapeChatControl gameChat = screen.findNiftyControl("gameChat", HexScapeChatControl.class);
		
		armiesPanelController = new ArmiesPanelController(nifty, screen);
		armiesPanelController.setPlayers(game.getPlayers());
		
		gameChatController = new GameChatController(game, gameChat);
		
		cameraButtonController = new CameraButtonController(screen);
		
		dicePanelController = new DicePanelController(nifty, screen);
		
		GuiMessageBus.register(armiesPanelController);
		GuiMessageBus.register(gameChatController);
		GuiMessageBus.register(cameraButtonController);
		GuiMessageBus.register(dicePanelController);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().lookAtTheMap();
	}
	
	@Override
	public void onEndScreen() {
		GuiMessageBus.unregister(armiesPanelController);
		GuiMessageBus.unregister(gameChatController);
		GuiMessageBus.unregister(cameraButtonController);
		GuiMessageBus.unregister(dicePanelController);
	}
	
	@NiftyEventSubscriber(id="gameChat")
	public void onChatTextSent(String id, HexScapeChatTextSendEvent event) {
		PostGameMessageMessage message = new PostGameMessageMessage(CurrentUserInfo.getInstance().getPlayerId(), event.getText(), game.getId());
	
		CoreMessageBus.post(message);
	}
	
}
