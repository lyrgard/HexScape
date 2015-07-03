package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.gui.desktop.controller.ImageButtonBuilder;
import fr.lyrgard.hexScape.message.CardInstanceChangedOwnerMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.MarkerService;

public class ArmyCardDetailController implements Controller {

	private Nifty nifty;
	private Screen screen;
	private Element armyCardImage;
	private Element ownerText;
	private Element markers;
	private Element actions;
	private Element actionsTitle;
	private Element markerActions;
	private Element hiddenMarkerActions;
	private Element giveToPlayerActions;
	private Element giveToPlayerActionsTitle;

	private CardInstance card;
	private Player owner;
	
	int i = 0;

	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
		this.nifty = nifty;
		this.screen = screen;
		armyCardImage = element.findElementByName("#armyCardImage");
		ownerText = element.findElementByName("#ownerText");
		markers = element.findElementByName("#markers");
		actions = element.findElementByName("#actions");
		actionsTitle = element.findElementByName("#actionsTitle");
		markerActions = element.findElementByName("#markerActions");
		hiddenMarkerActions = element.findElementByName("#hiddenMarkerActions");
		giveToPlayerActions = element.findElementByName("#giveToPlayerActions");
		giveToPlayerActionsTitle = element.findElementByName("#giveToPlayerActionsTitle");
	}

	@Override
	public void init(Properties parameter, Attributes controlDefinitionAttributes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocus(boolean getFocus) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCard(CardInstance card) {
		this.card = card;
		update();
	}

	private void update() {
		owner = CurrentUserInfo.getInstance().getGame().getCardOwner(card.getId());
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				String cardTypeId = card.getCardTypeId();
				CardType cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(cardTypeId);

				NiftyImage image = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), cardType.getImagePath(), true);
				armyCardImage.getRenderer(ImageRenderer.class).setImage(image);

				// Owner 
				for (Element text : ownerText.getElements()) {
					text.markForRemoval();
				}
				new TextBuilder() {{
					text("${i18n.cardOwner} : " + owner.getDisplayName());
					style("uiLabel24");
					valign(VAlign.Center);
				}}.build(nifty, screen, ownerText);

				// Markers on the card
				for (Element action : markers.getElements()) {
					action.markForRemoval();
				}
				for (MarkerInstance marker : card.getMarkers()) {
					displayMarker(marker);
				}

				// Actions
				for (Element action : markerActions.getElements()) {
					action.markForRemoval();
				}
				for (Element action : hiddenMarkerActions.getElements()) {
					action.markForRemoval();
				}
				if (owner.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
					actions.setVisible(true);
					actionsTitle.setVisible(true);
	
					Collection<MarkerDefinition> markerTypes = MarkerService.getInstance().getMarkersListForCard();
	
					for (final MarkerDefinition markerType : markerTypes) {
	
						switch(markerType.getType()) {
						case STACKABLE:
						case REVEALABLE:
						case NORMAL:
							displayMarkerTypeActions(markerType);
							break;
						case HIDDEN:
							displayHiddenMarkerTypeActions((HiddenMarkerDefinition)markerType);
							break;
						}
					}
	
					Game game = CurrentUserInfo.getInstance().getGame();
					if (game != null) {
						if (game.getPlayers().size() > 1) {
							for (Element action : giveToPlayerActions.getElements()) {
								action.markForRemoval();
							}
							
							boolean hasOnePlayerToGiveTo = false;
							for (Player player : game.getPlayers()) {
								if (player.getArmy() != null && !player.getArmy().hasCard(card.getId())) {
										addGiveCardToPlayerAction(player);
										hasOnePlayerToGiveTo = true;
								}
							}
							if (hasOnePlayerToGiveTo) {
								giveToPlayerActionsTitle.setVisible(true);
								giveToPlayerActions.setVisible(true);
							}
						} else {
							giveToPlayerActionsTitle.setVisible(false);
							giveToPlayerActions.setVisible(false);
						}
					}
				} else {
					actions.setVisible(false);
					actionsTitle.setVisible(false);
				}
				i++;
				return null;
			}
		});
	}

	private void addGiveCardToPlayerAction(final Player player) {
		new PanelBuilder() {{
			childLayoutHorizontal();
			margin("20px");
			text(new TextBuilder() {{
				text(player.getDisplayName());
				style("uiLabel24");
				textHAlignLeft();
				textVAlignCenter();
				valignCenter();
			}});
			panel(new PanelBuilder() {{
				childLayoutVertical();
				valign(VAlign.Center);
				control(new ImageButtonBuilder("#giveCardTo" + player.getId() + i) {{
					width("36px");
					height("32px");
					parameter("buttonImage", "/gui/images/giveCardSmall.png");
					parameter(GiveCardToPlayerButtonController.PLAYER_ID, player.getId());
					parameter(GiveCardToPlayerButtonController.CARD_ID, card.getId());
					controller("fr.lyrgard.hexScape.gui.desktop.controller.game.GiveCardToPlayerButtonController");
				}});
			}});
		}}.build(nifty, screen, giveToPlayerActions);
	}

	private void displayMarkerTypeActions(final MarkerDefinition markerType) {
		new PanelBuilder() {{
			childLayoutHorizontal();
			margin("20px");
			image(new ImageBuilder() {{
				height("50px");
				width("50px");
				filename(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(markerType.getImage().toURI()).getPath());
			}});
			panel(new PanelBuilder() {{
				childLayoutVertical();
				valign(VAlign.Center);
				control(new ImageButtonBuilder("#addMarker" + markerType.getId() + i) {{
					width("36px");
					height("32px");
					parameter("buttonImage", "/gui/images/addSmall.png");
					parameter(AddMarkerButtonController.MARKER_TYPE_ID, markerType.getId());
					parameter(AddMarkerButtonController.CARD_ID, card.getId());
					controller("fr.lyrgard.hexScape.gui.desktop.controller.game.AddMarkerButtonController");
				}});
			}});
		}}.build(nifty, screen, markerActions);
	}
	
	private void displayHiddenMarkerTypeActions(final HiddenMarkerDefinition markerType) {
		new PanelBuilder() {{
			childLayoutHorizontal();
			margin("20px");
			image(new ImageBuilder() {{
				height("50px");
				width("50px");
				filename(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(markerType.getImage().toURI()).getPath());
			}});
			panel(new PanelBuilder() {{
				childLayoutVertical();
				valign(VAlign.Center);
				control(new ImageButtonBuilder("#removeAllMarkers" + markerType.getId() + i) {{
					width("36px");
					height("32px");
					parameter("buttonImage", "/gui/images/removeAllSmall.png");
					parameter(RemoveAllMarkerOfMarkerTypeButtonController.MARKER_TYPE_ID, markerType.getId());
					controller("fr.lyrgard.hexScape.gui.desktop.controller.game.RemoveAllMarkerOfMarkerTypeButtonController");
				}});
			}});
		}}.build(nifty, screen, hiddenMarkerActions);
	}

	private void displayMarker(final MarkerInstance marker) {
		final MarkerDefinition markerType = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());
		new PanelBuilder() {{
			childLayoutHorizontal();
			margin("20px");
			if (marker instanceof StackableMarkerInstance) {
				text(new TextBuilder() {{
					text(String.valueOf(((StackableMarkerInstance)marker).getNumber()));
					style("uiLabel24");
					valign(VAlign.Center);
				}});
			}
			image(new ImageBuilder() {{
				valign(VAlign.Center);
				height("50px");
				width("50px");
				filename(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(markerType.getImage().toURI()).getPath());
				if (marker instanceof HiddenMarkerInstance && owner.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
					onHoverEffect(new HoverEffectBuilder("changeImage") {{
						String hiddenMarkerTypeId = ((HiddenMarkerInstance)marker).getHiddenMarkerDefinitionId();
						final MarkerDefinition hiddenMarkerType = MarkerService.getInstance().getMarkersByIds().get(hiddenMarkerTypeId);
						effectParameter("active", HexScapeCore.APP_DATA_FOLDER.toURI().relativize(hiddenMarkerType.getImage().toURI()).getPath());
						effectParameter("inactive", HexScapeCore.APP_DATA_FOLDER.toURI().relativize(markerType.getImage().toURI()).getPath());
					}});
				}
			}});
			if (owner.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
				panel(new PanelBuilder() {{
					childLayoutVertical();
					valign(VAlign.Center);
					if (marker instanceof StackableMarkerInstance) {
						control(new ImageButtonBuilder("#addMarker" + markerType.getId() + i) {{
							width("36px");
							height("32px");
							parameter("buttonImage", "/gui/images/addSmall.png");
							parameter(AddMarkerButtonController.MARKER_TYPE_ID, markerType.getId());
							parameter(AddMarkerButtonController.CARD_ID, card.getId());
							controller("fr.lyrgard.hexScape.gui.desktop.controller.game.AddMarkerButtonController");
						}});
					}
					if (marker instanceof HiddenMarkerInstance) {
						control(new ImageButtonBuilder("#revealMarker" + marker.getId() + i) {{
							width("36px");
							height("32px");
							parameter("buttonImage", "/gui/images/revealSmall.png");
							parameter(RevealMarkerButtonController.MARKER_ID, marker.getId());
							parameter(RevealMarkerButtonController.CARD_ID, card.getId());
							controller("fr.lyrgard.hexScape.gui.desktop.controller.game.RevealMarkerButtonController");
						}});
					}
					control(new ImageButtonBuilder("#removeMarker" + marker.getId() + i) {{
						width("36px");
						height("32px");
						parameter("buttonImage", "/gui/images/removeSmall.png");
						parameter(RemoveMarkerButtonController.MARKER_ID, marker.getId());
						parameter(AddMarkerButtonController.CARD_ID, card.getId());
						controller("fr.lyrgard.hexScape.gui.desktop.controller.game.RemoveMarkerButtonController");
					}});
				}});
			}
		}}.build(nifty, screen, markers);
	}

	@Subscribe public void onMarkerPlaced(MarkerPlacedMessage message) {
		String cardId = message.getCardId();

		if (card != null && card.getId().equals(cardId)) {
			update();
		}
	}

	@Subscribe public void onMarkerRevealed(MarkerRevealedMessage message) {
		String cardId = message.getCardId();

		if (card != null && card.getId().equals(cardId)) {
			update();
		}
	}

	@Subscribe public void onMarkerRemoved(MarkerRemovedMessage message) {
		String cardId = message.getCardId();

		if (card != null && card.getId().equals(cardId)) {
			update();
		}
	}
	
	@Subscribe public void onCardChangedOwner(CardInstanceChangedOwnerMessage message) {
		String cardId = message.getNewCardId();

		if (card != null && card.getId().equals(cardId)) {
			update();
		}
	}
}
