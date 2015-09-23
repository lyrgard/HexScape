package fr.lyrgard.hexScape.gui.desktop.controller.online;

import java.util.Properties;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.listbox.ListBoxImpl;
import de.lessvoid.nifty.controls.listbox.ListBoxItemController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;

public class GameItemController extends ListBoxItemController<String> {
	private static final String GAME_NAME_TEXT = "#gameNameText";
	private static final String GAME_DESCRIPTION_TEXT = "#gameDescriptionText";
	private static final String GAME_STATUS_IMAGE = "#gameStatusImage";
	private static final String PLAYER_NUMBER_TEXT = "#playerNumberText";
	
	private static NiftyImage GAME_STARTED_IMAGE;
	private static NiftyImage GAME_NOT_STARTED_IMAGE;
	
	private Nifty nifty;
	private Screen screen;
	
	private Game game;
		
	private Element rootElement;
	private Element gameStatusImage;
	private Element playerNumberText;

	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties properties, Attributes attributes) {
		rootElement = element;
		this.nifty= nifty;
		this.screen = screen;
	}
	
	public void setGame(String gameId) {
		game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game == null) {
			rootElement.setVisible(false);
			return;
		}
		
		if (GAME_STARTED_IMAGE == null) {
			synchronized (GameItemController.class) {
				if (GAME_STARTED_IMAGE == null) {
					GAME_STARTED_IMAGE = nifty.getRenderEngine().createImage(screen, "gui/images/gameStatusPlaying.png", false);
				}
				if (GAME_NOT_STARTED_IMAGE == null) {
					GAME_NOT_STARTED_IMAGE = nifty.getRenderEngine().createImage(screen, "gui/images/gameStatusStoped.png", false);
				}
			}
		}
		
		
		Element gameNameText = rootElement.findElementByName(GAME_NAME_TEXT);
		gameNameText.getRenderer(TextRenderer.class).setText(game.getName());
		
		Element gameDescriptionText = rootElement.findElementByName(GAME_DESCRIPTION_TEXT);
		gameDescriptionText.getRenderer(TextRenderer.class).setText(game.getDescription());
		
		gameStatusImage = rootElement.findElementByName(GAME_STATUS_IMAGE);
		updateGameStatusImage();
		
		playerNumberText = rootElement.findElementByName(PLAYER_NUMBER_TEXT);
		updatePlayerNumberText();
				
		GuiMessageBus.register(this);
		
		rootElement.layoutElements();
	}
	
	private void updatePlayerNumberText() {
		playerNumberText.getRenderer(TextRenderer.class).setText("${i18n.players} : " + game.getNonFreePlayersNumber() + "/" + game.getPlayerNumber() );
	}
	
	private void updateGameStatusImage() {
		if (game.isStarted()) {
			gameStatusImage.getRenderer(ImageRenderer.class).setImage(GAME_STARTED_IMAGE);
		} else {
			gameStatusImage.getRenderer(ImageRenderer.class).setImage(GAME_NOT_STARTED_IMAGE);
		}
	}

	@Override
	public void init(Properties properties, Attributes attributes) {
	}

	@Override
	public boolean inputEvent(NiftyInputEvent arg0) {
		return false;
	}

	@Override
	public void onFocus(boolean arg0) {
	}

	@Override
	public void onStartScreen() {
	}
	
	
	public void listBoxItemClicked() {
        String item = getListBox().getItemByVisualIndex(getVisualItemIndex());
        getListBox().setFocusItem(item);
        if (getListBox().getSelection().contains(item)) {
        	getListBox().deselectItemByVisualIndex(getVisualItemIndex());
        } else {
        	getListBox().selectItemByVisualIndex(getVisualItemIndex());
        }
    }
	

	@Subscribe public void onGameStarted(final GameStartedMessage message) {
		String gameId = message.getGameId();
		
		if (gameId.equals(game.getId())) {
			updateGameStatusImage();
		}
	}
	
	@Subscribe public void onGameJoined(final GameJoinedMessage message) {
		Game game = message.getGame();
		
		if (game.getId().equals(this.game.getId())) {
			updatePlayerNumberText();
		}
	}
	
	@Subscribe public void onGameLeft(final GameLeftMessage message) {
		String gameId = message.getGameId();
		
		if (gameId.equals(game.getId())) {
			updatePlayerNumberText();
		}
	}
}
