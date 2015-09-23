package fr.lyrgard.hexScape.gui.desktop.controller.online;

import java.awt.EventQueue;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.camera.RotatingAroundCamera;
import fr.lyrgard.hexScape.camera.RotatingAroundCameraAppState;
import fr.lyrgard.hexScape.gui.desktop.controller.ImageButtonTextBuilder;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatControl;
import fr.lyrgard.hexScape.gui.desktop.controller.chat.HexScapeChatTextSendEvent;
import fr.lyrgard.hexScape.gui.desktop.controller.game.GameProperties;
import fr.lyrgard.hexScape.gui.desktop.controller.loadGame.ChoosePlayerButtonController;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameObservedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.LeaveGameMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.message.ObserveGameMessage;
import fr.lyrgard.hexScape.message.PostRoomMessageMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.TitleScreen;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;
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
	
	private SelectedGameController selectedGameController;
	private Game selectedGame;
	private Element choosePlayerPopup;
	
	private ViewPort backgroundViewPort;
	
	private ListBox<String> gameList;

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
		
		selectedGameController = screen.findControl("selectedGame", SelectedGameController.class);
		
		gameList = screen.findNiftyControl("gameList", ListBox.class);
		
		gameList.removeAllItems(gameList.getItems());
		
		for (final Game game : Universe.getInstance().getGamesByGameIds().values()) {
			addGame(game);
		}
		
		if (!Universe.getInstance().getGamesByGameIds().values().isEmpty()) {
			Game firstGame = Universe.getInstance().getGamesByGameIds().values().iterator().next(); 
			gameList.selectItem(firstGame.getId());
			selectGame(firstGame);
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
		this.map = null;
		selectGame(null);
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
		if (choosePlayerPopup != null && choosePlayerPopup.isVisible()) {
			nifty.closePopup(choosePlayerPopup.getId());
			choosePlayerPopup = null;
		}
	}
	
	public void addGame(Game game) {
//		new ControlBuilder("gameItem") {{
//			parameter(GameProperties.GAME_ID, gameId);
//		}}.build(nifty, screen, gameListContent);
		gameList.addItem(game.getId());
	}
	
	private void selectGame(Game game) {
		this.selectedGame = game;
		if (game != null) {
			map = game.getMap();
			selectedGameController.setGame(game.getId());
		} else {
			selectedGameController.setGame(null);
		}
	}
	
	public void joinSelectedGame() {
		if (selectedGame.getFreePlayers().size() == 1) {
			Player player = selectedGame.getFreePlayers().iterator().next();
			joinSelectedGameAsPlayer(player.getId());
		} else {
			openChoosePlayerPopup(selectedGame);
		}
	}
	
	public void joinSelectedGameAsPlayer(String playerId) {
		JoinGameMessage message = new JoinGameMessage(CurrentUserInfo.getInstance().getId(), selectedGame.getId(), playerId);
		CoreMessageBus.post(message);
		closePopups();
	}
	
	public void leaveSelectedGame() {
		LeaveGameMessage message = new LeaveGameMessage();
		CoreMessageBus.post(message);
	}
	
	public void startSelectedGame() {
		StartGameMessage message = new StartGameMessage(CurrentUserInfo.getInstance().getPlayerId(), selectedGame.getId());
		CoreMessageBus.post(message);
	}
	
	public void observeSelectedGame() {
		ObserveGameMessage message = new ObserveGameMessage(CurrentUserInfo.getInstance().getId(), selectedGame.getId());
		CoreMessageBus.post(message);
	}
	
	public void openChoosePlayerPopup(final Game game) {
		closePopups();
		
		new PopupBuilder("choosePlayerPopup") {{
			childLayoutCenter();
			backgroundColor("#0000");
			panel(new PanelBuilder() {{
				height("400px");
				width("800px");
				childLayoutCenter();
				image(new ImageBuilder() {{
					filename("gui/images/textfield/textfieldBackground.png");
					imageMode("resize:10,8,10,15,6,16,6,2,10,8,10,15");
					width("100%");
					height("100%");					
				}});
				panel(new PanelBuilder("") {{
					width("100%");
					height("100%");
					padding("10px");
					childLayoutVertical();
					text(new TextBuilder() {{
						text("${i18n.joinAs} :");
						style("uiLabel24");
					}});
					panel(new PanelBuilder("choosePlayerButtonsContainer") {{
						width("100%");
						height("*");
						set("childLayout", "evenly-distributed");
						for (final Player player : game.getFreePlayers()) {
							control(new ImageButtonTextBuilder("choosePlayerButton_" + player.getId()) {{
								parameter("image", "gui/images/startGameWithText.png");
								parameter("imageHover", "gui/images/startGameWithText.png");
								parameter("imagePressed", "gui/images/startGameWithText.png");
								parameter("text", player.getDisplayName());
								parameter(ChoosePlayerButtonController.PLAYER_ID, player.getId());
								parameter(ChoosePlayerButtonController.GAME_ID, game.getId());
								parameter("textWidth", "250px");
								width("290px");
								height("48px");
								alignCenter();
								controller("fr.lyrgard.hexScape.gui.desktop.controller.online.ChoosePlayerButtonController");
							}});
						}
					}});
				}});
			}});
			
		}}.registerPopup(nifty);
		
		choosePlayerPopup = nifty.createPopup("choosePlayerPopup");
		nifty.showPopup(nifty.getCurrentScreen(), choosePlayerPopup.getId(), null);
		
		Element container = choosePlayerPopup.findElementByName("choosePlayerButtonsContainer");
		
		choosePlayerPopup.resetLayout();
		container.resetLayout();

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
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		final String gameId = message.getGameId();
		final String userId = message.getUserId();
		
		if (!userId.equals(CurrentUserInfo.getInstance().getId())) {
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			User user = Universe.getInstance().getUsersByIds().get(userId);

			if (user != null && game != null) {
				
			}
		}
	}
	
	@Subscribe public void onGameStarted(GameStartedMessage message) {
		final String gameId = message.getGameId();
		if (gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			nifty.gotoScreen("gameScreen");
		}
	}
	
	@Subscribe public void onGameJoined(final GameJoinedMessage message) {
		final String gameId = message.getGame().getId();
		final String playerId = message.getPlayerId();
		if (gameId.equals(CurrentUserInfo.getInstance().getGameId()) && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			if (game.isStarted()) {
				nifty.gotoScreen("gameScreen");
			}
		}
	}
	
	@Subscribe public void onGameObserved(final GameObservedMessage message) {
		final String gameId = message.getGameId();
		if (gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			if (game.isStarted()) {
				nifty.gotoScreen("gameScreen");
			}
		}
	}
	
	@Subscribe public void onGameEnded(GameEndedMessage message) {
		String gameId = message.getGameId();
		
		gameList.removeItem(gameId);
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

	/**
	 * When the selection of the ListBox changes this method is called.
	 */
	@NiftyEventSubscriber(id="gameList")
	public void onMyListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
		List<String> selection = event.getSelection();
		for (String gameId : selection) {
			Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
			if (game != null) {
				selectGame(game);
			}
		}
	}
}
