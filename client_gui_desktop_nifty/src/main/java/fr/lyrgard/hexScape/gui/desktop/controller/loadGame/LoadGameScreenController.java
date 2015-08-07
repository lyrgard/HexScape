package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.ImageButtonTextBuilder;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.player.Player;

public class LoadGameScreenController implements ScreenController {

	private String gameName;
	private Map map;
	private int playerNumber;
	
	private Nifty nifty;
	private Screen screen;
	private DropDown<Integer> playerNumberDropDown;
	
	private Element newGameOrRestoreGamePopup;
	private Element newGamePopup;
	private Element choosePlayerPopup;
	
	private Element mapNameDisplay;
	private Element mapNameText;
	
	
	private Element startGameButton;

	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
		
		mapNameDisplay = screen.findElementByName("mapNameDisplay");
		mapNameText = screen.findElementByName("mapNameText");
	}
	
	@Override
	public void onStartScreen() {
		//HexScapeCore.getInstance().getHexScapeJme3Application().setScene(null);
		mapNameDisplay.setVisible(false);
		map = null;
		newGameOrRestoreGamePopup = nifty.createPopup("newGameOrRestoreGamePopup");
		nifty.showPopup(nifty.getCurrentScreen(), newGameOrRestoreGamePopup.getId(), null);
		GuiMessageBus.register(this);
	}
	
	@Override
	public void onEndScreen() {
		closePopups();
		GuiMessageBus.unregister(this);
	}
	
	
	public void checkGameReadyToStart() {
		if (startGameButton != null) {
			if (StringUtils.isNotEmpty(gameName) && map != null) {
				startGameButton.setVisible(true);
			} else {
				startGameButton.setVisible(false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void openNewGamePopup() {
		closePopups();
		newGamePopup = nifty.createPopup("newGamePopup");
		nifty.showPopup(nifty.getCurrentScreen(), newGamePopup.getId(), null);
		
		playerNumberDropDown = newGamePopup.findNiftyControl("playerNumberDropDown", DropDown.class);
		playerNumberDropDown.addItem(1);
		playerNumberDropDown.addItem(2);
		playerNumberDropDown.addItem(3);
		playerNumberDropDown.addItem(4);
		playerNumber = playerNumberDropDown.getSelection();
		
		startGameButton = newGamePopup.findElementByName("startGameButton");
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
								parameter("textWidth", "200px");
								width("240px");
								height("48px");
								alignCenter();
								controller("fr.lyrgard.hexScape.gui.desktop.controller.loadGame.ChoosePlayerButtonController");
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
	
	public void closePopups() {
		if (newGameOrRestoreGamePopup != null && newGameOrRestoreGamePopup.isVisible()) {
			nifty.closePopup(newGameOrRestoreGamePopup.getId());
			newGameOrRestoreGamePopup = null;
		}
		if (newGamePopup != null && newGamePopup.isVisible()) {
			nifty.closePopup(newGamePopup.getId());
			newGamePopup = null;
		}
		if (choosePlayerPopup != null && choosePlayerPopup.isVisible()) {
			nifty.closePopup(choosePlayerPopup.getId());
			choosePlayerPopup = null;
		}
	}
	
	public void startGame() {
		closePopups();
		playerNumber = playerNumberDropDown.getSelection();
		CreateGameMessage message = new CreateGameMessage(gameName, map, playerNumber);
		CoreMessageBus.post(message);
	}
	
	
	@Subscribe public void onMapLoaded(final MapLoadedMessage message) {
		Map map = message.getMap();
		setMap(map);
		checkGameReadyToStart();
	}
	
	private void setMap(Map map) {
		mapNameText.getRenderer(TextRenderer.class).setText(map.getName());
		mapNameDisplay.setVisible(true);
		LoadGameScreenController.this.map = map;
	}
	
	@Subscribe public void onGameCreated(final GameCreatedMessage message) {
		Game game = message.getGame();
		
		setMap(game.getMap());
		
		DisplayMapMessage displayMapMessage = new DisplayMapMessage(game.getId(), true);
		CoreMessageBus.post(displayMapMessage);
		
		StartGameMessage startGameMessage = new StartGameMessage(CurrentUserInfo.getInstance().getId(), game.getId());
		CoreMessageBus.post(startGameMessage);
	}
	
	@Subscribe public void onGameStarted(final GameStartedMessage message) {
		String gameId = message.getGameId();
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null) {
			if (!game.getFreePlayers().isEmpty()) {
				if (game.getFreePlayers().size() > 1) {
					openChoosePlayerPopup(game);
				} else {
					Player player = game.getFreePlayers().iterator().next();
					JoinGameMessage joinGameMessage = new JoinGameMessage(CurrentUserInfo.getInstance().getId(), gameId, player.getId());
					CoreMessageBus.post(joinGameMessage);
				}
			} else {
				
			}
			
		}
	}
	
	
	@Subscribe public void onGameJoined(final GameJoinedMessage message) {
		nifty.gotoScreen("gameScreen");
	}

	@NiftyEventSubscriber(id="gameNameTextField")
	public void onGameNameChanged(String id, TextFieldChangedEvent event) {
		gameName = event.getText();
		if (StringUtils.isBlank(gameName)) {
			gameName = null;
		}
		checkGameReadyToStart();
	}
	
	@NiftyEventSubscriber(id="playerNumberDropDown")
	public void onPlayerNumberChanged(String id, DropDownSelectionChangedEvent<Integer> event) {
		playerNumber = event.getSelection();
	}
	
	@NiftyEventSubscriber(id="cancelNewGame")
	public void onCancelNewGame(String id, NiftyMousePrimaryClickedEvent event) {
		closePopups();
		HexScapeCore.getInstance().getHexScapeJme3Application().displayTitleScreen();
		mapNameDisplay.setVisible(false);
		map = null;
		newGameOrRestoreGamePopup = nifty.createPopup("newGameOrRestoreGamePopup");
		nifty.showPopup(nifty.getCurrentScreen(), newGameOrRestoreGamePopup.getId(), null);
	}
	
	@NiftyEventSubscriber(id="cancelNewOrLoad")
	public void onCancelNewOrLoad(String id, NiftyMousePrimaryClickedEvent event) {
		nifty.gotoScreen("homeScreen");
	}
	
	
}
