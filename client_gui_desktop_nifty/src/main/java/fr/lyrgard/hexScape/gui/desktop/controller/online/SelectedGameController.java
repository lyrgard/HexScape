package fr.lyrgard.hexScape.gui.desktop.controller.online;

import java.util.Properties;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class SelectedGameController extends AbstractController {

	private TextRenderer selectedGameName;
	//private TextRenderer selectedGameDescription;
	private Element joinSelectedGameButton;
	private Element leaveSelectedGameButton;
	private Element startSelectedGameButton;
	private Element observeSelectedGameButton;
	private ListBox<Player> playerList;
	
	private Game game;
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
		selectedGameName = screen.findElementByName("selectedGameName").getRenderer(TextRenderer.class);
		//selectedGameDescription = screen.findElementByName("selectedGameDescription").getRenderer(TextRenderer.class);
		joinSelectedGameButton = screen.findElementByName("joinSelectedGameButton");
		leaveSelectedGameButton = screen.findElementByName("leaveSelectedGameButton");
		startSelectedGameButton = screen.findElementByName("startSelectedGameButton");
		observeSelectedGameButton = screen.findElementByName("observeSelectedGameButton");
		playerList = screen.findNiftyControl("selectedGamePlayerList", ListBox.class);
		
		setGame(null);
		
		GuiMessageBus.register(this);
	}

	@Override
	public void onStartScreen() {
		
	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		return false;
	}
	
	public void setGame(String gameId) {
		game = null;
		if (gameId != null) {
			game = Universe.getInstance().getGamesByGameIds().get(gameId);
		}
		playerList.removeAllItems(playerList.getItems());
		refreshSelectedGameAction();
		if (game == null) {
			selectedGameName.setText(" ");
			HexScapeCore.getInstance().getHexScapeJme3Application().displayBlankScreen();
			//selectedGameDescription.setText(" ");
		} else {
			selectedGameName.setText(game.getName());
			//selectedGameDescription.setText(game.getDescription());
			DisplayMapMessage displayMapMessage = new DisplayMapMessage(game.getMap());
			CoreMessageBus.post(displayMapMessage);
			for (Player player : game.getPlayers()) {
				playerList.addItem(player);
			}
		}
	}
	
	private void refreshSelectedGameAction() {
		boolean canJoin = false;
		boolean canStart = false;
		boolean isInTheGame = false;
		boolean canObserve = false;
		if (game != null) {
			canJoin = !game.getFreePlayers().isEmpty();
			for (Player player : game.getPlayers()) {
				if (game.getId().equals(CurrentUserInfo.getInstance().getGameId()) && player.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
					canJoin = false;
					canStart = !game.isStarted();
					isInTheGame = true;
				}
			}
			if (canJoin) {
				if (CurrentUserInfo.getInstance().getGameId() != null) {
					canJoin = false;
				}
			}
			canObserve = !isInTheGame && game.isStarted();
		}
		joinSelectedGameButton.setVisible(canJoin);
		joinSelectedGameButton.setConstraintY(new SizeValue(getButtonY()));
		leaveSelectedGameButton.setVisible(isInTheGame);
		leaveSelectedGameButton.setConstraintY(new SizeValue(getButtonY(canJoin)));
		startSelectedGameButton.setVisible(canStart);
		startSelectedGameButton.setConstraintY(new SizeValue(getButtonY(canJoin, isInTheGame)));
		observeSelectedGameButton.setVisible(canObserve);
		observeSelectedGameButton.setConstraintY(new SizeValue(getButtonY(canJoin, isInTheGame, canStart)));
		joinSelectedGameButton.getParent().layoutElements();
	}
	
	private String getButtonY(boolean... previousButtonDisplayed) {
		int result = 5;
		for (boolean displayed : previousButtonDisplayed) {
			if (displayed) {
				result += (48 + 5);
			}
		}
		return String.valueOf(result);
	}
	
	@Subscribe public void onGameStarted(final GameStartedMessage message) {
		String gameId = message.getGameId();
		
		if (gameId.equals(game.getId())) {
			refreshSelectedGameAction();
		}
	}
	
	@Subscribe public void onGameJoined(final GameJoinedMessage message) {
		Game game = message.getGame();
		
		if (game.getId().equals(this.game.getId())) {
			setGame(game.getId());
		}
	}
	
	@Subscribe public void onGameLeft(final GameLeftMessage message) {
		String gameId = message.getGameId();
		
		if (gameId.equals(game.getId())) {
			refreshSelectedGameAction();
		}
	}

	@Subscribe public void onGameEnded(GameEndedMessage message) {
		String gameId = message.getGameId();
		
		if (gameId.equals(game.getId())) {
			setGame(null);
		}
	}
}
