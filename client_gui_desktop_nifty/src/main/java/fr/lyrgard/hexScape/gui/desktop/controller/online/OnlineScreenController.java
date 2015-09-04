package fr.lyrgard.hexScape.gui.desktop.controller.online;

import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.camera.RotatingAroundCamera;
import fr.lyrgard.hexScape.camera.RotatingAroundCameraAppState;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatTextSendEvent;
import fr.lyrgard.hexScape.gui.desktop.controller.game.GameProperties;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.message.PostRoomMessageMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.TitleScreen;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.service.MapManager;

public class OnlineScreenController implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private Room room;
	private RoomChatController roomChatController;
	
	private Element newOnlineGamePopup;
	private DropDown<Integer> playerNumberDropDown;
	private int playerNumber;
	private String gameName;
	private String gameDescription;
	private Map map;
	private Element createOnlineGameButton;
	
	private ViewPort backgroundViewPort;
	
	private ListBox<Game> gameList;

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	public void disconnectFromServer() {
		DisconnectFromServerMessage message = new DisconnectFromServerMessage();
		CoreMessageBus.post(message);
	}

	@Override
	public void onStartScreen() {
		room = CurrentUserInfo.getInstance().getRoom();
		
		HexScapeChatControl roomChat = screen.findNiftyControl("roomChat", HexScapeChatControl.class);
		
		roomChatController = new RoomChatController(room, roomChat);
		
		GuiMessageBus.register(roomChatController);
		GuiMessageBus.register(this);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				Camera cam2 = HexScapeCore.getInstance().getHexScapeJme3Application().getCamera().clone();
				backgroundViewPort = HexScapeCore.getInstance().getHexScapeJme3Application().getRenderManager().createPreView("background", cam2);
				backgroundViewPort.setClearFlags(true, true, true);
				backgroundViewPort.clearScenes();
				backgroundViewPort.attachScene(TitleScreen.getInstance().getSpatial());
				HexScapeCore.getInstance().getHexScapeJme3Application().getCamera().setViewPort(0.5f, 0.98f, 0.04f, 0.48f);
				HexScapeCore.getInstance().getHexScapeJme3Application().displayBlankScreen();
				
				return null;
			}
		});
		
		gameList = screen.findNiftyControl("gameList", ListBox.class);
		
		
		for (final Game game : Universe.getInstance().getGamesByGameIds().values()) {
			addGame(game);
		}
	}
	
	@Override
	public void onEndScreen() {
		GuiMessageBus.unregister(this);
		GuiMessageBus.unregister(roomChatController);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeCore.getInstance().getHexScapeJme3Application().getCamera().setViewPort(0f, 1f, 0f, 1f);
				HexScapeCore.getInstance().getHexScapeJme3Application().getRenderManager().removePreView(backgroundViewPort);
				return null;
			}
		});
		
	}
	
	public void checkGameReadyToStart() {
		if (createOnlineGameButton != null) {
			if (StringUtils.isNotEmpty(gameName) && map != null) {
				createOnlineGameButton.setVisible(true);
			} else {
				createOnlineGameButton.setVisible(false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void openNewOnlineGamePopup() {
		closePopups();
		newOnlineGamePopup = nifty.createPopup("newOnlineGamePopup");
		nifty.showPopup(nifty.getCurrentScreen(), newOnlineGamePopup.getId(), null);
		
		playerNumberDropDown = newOnlineGamePopup.findNiftyControl("onlineGamePlayerNumberDropDown", DropDown.class);
		playerNumberDropDown.addItem(2);
		playerNumberDropDown.addItem(3);
		playerNumberDropDown.addItem(4);
		playerNumberDropDown.selectItemByIndex(0);
		playerNumber = playerNumberDropDown.getSelection();
		
		createOnlineGameButton = newOnlineGamePopup.findElementByName("createOnlineGameButton");
	}
	
	public void createOnlineGame() {
		closePopups();
		playerNumber = playerNumberDropDown.getSelection();
		CreateGameMessage message = new CreateGameMessage(gameName, gameDescription, map, playerNumber);
		CoreMessageBus.post(message);
	}
	
	public void closePopups() {
		if (newOnlineGamePopup != null && newOnlineGamePopup.isVisible()) {
			nifty.closePopup(newOnlineGamePopup.getId());
			newOnlineGamePopup = null;
		}
	}
	
	public void addGame(Game game) {
//		new ControlBuilder("gameItem") {{
//			parameter(GameProperties.GAME_ID, gameId);
//		}}.build(nifty, screen, gameListContent);
		gameList.addItem(game);
	}
	
	@Subscribe public void onDisconnectedFromServer(DisconnectedFromServerMessage message) {
		if (message.getUserId().equals(CurrentUserInfo.getInstance().getId())) {
			nifty.gotoScreen("homeScreen");
		}
	}
	
	@Subscribe public void onMapLoaded(final MapLoadedMessage message) {
		map = message.getMap();
		DisplayMapMessage displayMapMessage = new DisplayMapMessage(map);
		CoreMessageBus.post(displayMapMessage);
		checkGameReadyToStart();
	}
	
	@Subscribe public void onGameCreated(final GameCreatedMessage message) {
		final Game game = message.getGame();
		
		addGame(game);
	}
	
	@NiftyEventSubscriber(id="onlineGameNameTextField")
	public void onGameNameChanged(String id, TextFieldChangedEvent event) {
		gameName = event.getText();
		if (StringUtils.isBlank(gameName)) {
			gameName = null;
		}
		checkGameReadyToStart();
	}
	
	@NiftyEventSubscriber(id="onlineGameDescriptionTextField")
	public void onGameDescriptionChanged(String id, TextFieldChangedEvent event) {
		gameDescription = event.getText();
		if (StringUtils.isBlank(gameDescription)) {
			gameDescription = null;
		}
	}
	
	@NiftyEventSubscriber(id="onlineGamePlayerNumberDropDown")
	public void onPlayerNumberChanged(String id, DropDownSelectionChangedEvent<Integer> event) {
		playerNumber = event.getSelection();
	}
	
	@NiftyEventSubscriber(id="cancelNewOnlineGame")
	public void onCancelNewGame(String id, NiftyMousePrimaryClickedEvent event) {
		closePopups();
		HexScapeCore.getInstance().getHexScapeJme3Application().displayBlankScreen();
	}
	
	@NiftyEventSubscriber(id="roomChat")
	public void onChatTextSent(String id, HexScapeChatTextSendEvent event) {
		if (room != null) {
			PostRoomMessageMessage message = new PostRoomMessageMessage(CurrentUserInfo.getInstance().getPlayerId(), event.getText(), room.getId());

			CoreMessageBus.post(message);
		}
	}
}
