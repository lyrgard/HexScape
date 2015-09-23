package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatTextSendEvent;
import fr.lyrgard.hexScape.gui.desktop.message.DisplayCardDetailMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
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
	private Element cardDetailPopup;
	private ArmyCardDetailController armyCardDetailController;
	
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
		GuiMessageBus.register(this);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().lookAtTheMap();
		
		new PopupBuilder("cardDetailPopup") {{
			childLayoutCenter();
			backgroundColor("#0005");
			control(new ControlBuilder("armyCardDetail", "armyCardDetail"));
			interactOnClick("closeDetail()");
		}}.registerPopup(nifty);
		
		cardDetailPopup = nifty.createPopup("cardDetailPopup");
		armyCardDetailController = cardDetailPopup.findControl("armyCardDetail", ArmyCardDetailController.class);
	}
	
	@Override
	public void onEndScreen() {
		
		armiesPanelController.unload();
		
		GuiMessageBus.unregister(armiesPanelController);
		GuiMessageBus.unregister(gameChatController);
		GuiMessageBus.unregister(cameraButtonController);
		GuiMessageBus.unregister(dicePanelController);
		GuiMessageBus.unregister(this);
	}
	
	@NiftyEventSubscriber(id="gameChat")
	public void onChatTextSent(String id, HexScapeChatTextSendEvent event) {
		PostGameMessageMessage message = new PostGameMessageMessage(CurrentUserInfo.getInstance().getPlayerId(), event.getText(), game.getId());
	
		CoreMessageBus.post(message);
	}
	
	@Subscribe public void onDisplayCardDetail(DisplayCardDetailMessage message) {
		armyCardDetailController.setCard(message.getCard());
		GuiMessageBus.register(armyCardDetailController);
		nifty.showPopup(screen, cardDetailPopup.getId(), null);
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		String gameId = message.getGameId();
		
		if (gameId.equals(game.getId()) && CurrentUserInfo.getInstance().getPlayer() == null) {
			// Current game && the player is null == the current user left the game
			HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					if (HexScapeCore.getInstance().isOnline()) {
						nifty.gotoScreen("online");
					} else {
						HexScapeCore.getInstance().getHexScapeJme3Application().displayTitleScreen();
						nifty.gotoScreen("homeScreen");
					}
					return null;
				}
			});
		}
	}
	
	public void closeDetail() {
		GuiMessageBus.unregister(armyCardDetailController);
		nifty.closePopup(cardDetailPopup.getId());
	}
	
}
