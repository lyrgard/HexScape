package fr.lyrgard.hexScape.gui.desktop.controller.loadGame;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.CreateGameMessage;
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
	public void onEndScreen() {
		if (newGameOrRestoreGamePopup != null && newGameOrRestoreGamePopup.isVisible()) {
			nifty.closePopup(newGameOrRestoreGamePopup.getId());
		}
		GuiMessageBus.unregister(this);
	}

	@Override
	public void onStartScreen() {
		//HexScapeCore.getInstance().getHexScapeJme3Application().setScene(null);
		newGameOrRestoreGamePopup = nifty.createPopup("newGameOrRestoreGamePopup");
		nifty.showPopup(nifty.getCurrentScreen(), newGameOrRestoreGamePopup.getId(), null);
		GuiMessageBus.register(this);
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
		if (newGameOrRestoreGamePopup != null && newGameOrRestoreGamePopup.isVisible()) {
			nifty.closePopup(newGameOrRestoreGamePopup.getId());
		}
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
	
	public void startGame() {
		if (newGamePopup != null && newGamePopup.isVisible()) {
			nifty.closePopup(newGamePopup.getId());
		}
		playerNumber = playerNumberDropDown.getSelection();
		CreateGameMessage message = new CreateGameMessage(gameName, map, playerNumber);
		CoreMessageBus.post(message);
	}
	
	
	@Subscribe public void onMapLoaded(final MapLoadedMessage message) {
		Map map = message.getMap();
		mapNameText.getRenderer(TextRenderer.class).setText(map.getName());
		mapNameDisplay.setVisible(true);
		LoadGameScreenController.this.map = map;
		checkGameReadyToStart();
	}
	
	@Subscribe public void onGameCreated(final GameCreatedMessage message) {
		Game game = message.getGame();
		StartGameMessage startGameMessage = new StartGameMessage(CurrentUserInfo.getInstance().getId(), game.getId());
		CoreMessageBus.post(startGameMessage);
	}
	
	@Subscribe public void onGameStarted(final GameStartedMessage message) {
		String gameId = message.getGameId();
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null) {
			if (!game.getFreePlayers().isEmpty()) {
				Player player = game.getFreePlayers().iterator().next();
				JoinGameMessage joinGameMessage = new JoinGameMessage(CurrentUserInfo.getInstance().getId(), gameId, player.getId());
				CoreMessageBus.post(joinGameMessage);
			} else {
				
			}
			
		}
	}
	
	@Subscribe public void onGameJoined(final GameJoinedMessage message) {
		nifty.gotoScreen("gameScreen");
	}

	@NiftyEventSubscriber(id="gameNameTextField#textField")
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
	
	
}
