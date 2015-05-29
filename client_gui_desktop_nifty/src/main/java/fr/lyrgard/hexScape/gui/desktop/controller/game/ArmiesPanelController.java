package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import fr.lyrgard.hexScape.gui.desktop.controller.ImageButtonBuilder;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.CardService;

public class ArmiesPanelController {

	private Nifty nifty;
	private Screen screen;
	private Collection<Player> players;
	private ScrollPanel scrollPanel;
	private Element scrollPanelContainer;
	private Map<String, Element> loadArmyButtons = new HashMap<>();

	public ArmiesPanelController(Nifty nifty, Screen screen, ScrollPanel scrollPanel) {
		this.nifty = nifty;
		this.screen = screen;
		this.scrollPanel = scrollPanel;
		scrollPanelContainer = scrollPanel.getElement().findElementByName(scrollPanel.getId() + "#nifty-scrollpanel-child-root#scrollPanelContainer");
	}

	public void setPlayers(Collection<Player> players) {
		this.players = players;

		
		final Player player = players.iterator().next();
		
		Element loadArmyButton = new ImageButtonBuilder() {{
			parameter("image", "gui/icons/chooseArmy.png");
			parameter("imageHover", "gui/icons/chooseArmyHover.png");
			parameter("imagePressed", "gui/icons/chooseArmy.png");
			parameter("text", "CHANGE_ME");
			parameter(ChooseArmyButtonController.PLAYER_ID_PROPERTY, player.getId());
			width("100px");
			height("24px");
			alignCenter();
			controller(new ChooseArmyButtonController());
		}}.build(nifty, screen, scrollPanelContainer);
		loadArmyButtons.put(player.getId(), loadArmyButton);
	}
	
	@Subscribe
	public void onArmyLoaded(ArmyLoadedMessage message) {
		String playerId = message.getPlayerId();
		Army army = message.getArmy();
		
		Element loadArmyButton = loadArmyButtons.get(playerId);
		if (loadArmyButton != null) {
			loadArmyButton.markForRemoval();
		}
		
		int i = 0;
		for (CardInstance card : army.getCards()) {
			final CardType cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());
			new ArmyPanelBuilder(card, playerId, i) {{
				height("100px");
				parameter("image", cardType.getIconPath());
			}}.build(nifty, screen, scrollPanelContainer);
			i++;
		}
	}
}
