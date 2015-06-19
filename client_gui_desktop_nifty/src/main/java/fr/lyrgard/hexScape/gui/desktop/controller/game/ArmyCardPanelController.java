package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.Collection;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.AddMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.AddPieceAction;
import fr.lyrgard.hexScape.gui.desktop.action.AddStackableMarkerToCardAction;
import fr.lyrgard.hexScape.gui.desktop.action.ChangeCardOwnerAction;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseMapAction;
import fr.lyrgard.hexScape.gui.desktop.action.RemoveAllMarkersOfTypeForPlayerdAction;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.RevealableMarkerDefinition;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.MarkerService;

public class ArmyCardPanelController implements Controller {
	private static final String CARD_NAME_TEXT = "#cardNameText";
	private static final String FIGURES_NUMBER_TEXT = "#figuresNumberText";
	private static final String ADD_ALL_PIECES_BUTTON = "#putOnMapButton";
	
	private Nifty nifty;
	private Screen screen;
	
	private CardInstance card;
	private CardType cardType;
	
	private Element addAllPiecesButton;
	
	private Element figuresNumberText;

	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties properties, Attributes attributes) {
		this.nifty = nifty;
		this.screen = screen;
		String playerId = attributes.get(GameProperties.PLAYER_ID);
		String cardId = attributes.get(GameProperties.CARD_ID);
		
		Player player = CurrentUserInfo.getInstance().getGame().getPlayer(playerId);
		
		card = player.getArmy().getCard(cardId);
		cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());

			
		Element cardNameText = element.findElementByName(CARD_NAME_TEXT);
		cardNameText.getRenderer(TextRenderer.class).setText(cardType.getName());
		
		figuresNumberText = element.findElementByName(FIGURES_NUMBER_TEXT);
		updateFiguresNumberText();
		
		
		addAllPiecesButton = element.findElementByName(ADD_ALL_PIECES_BUTTON);
		PlaceAllPiecesButtonController addAllPiecesButtonController = addAllPiecesButton.getControl(PlaceAllPiecesButtonController.class);
		addAllPiecesButtonController.setCard(card);
		
		
		
		GuiMessageBus.register(this);
	}
	
	private void updateFiguresNumberText() {
		figuresNumberText.getRenderer(TextRenderer.class).setText(card.getPieceLeftToPlace().size() + "/" + card.getNumber() * cardType.getFigureNames().size() );
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

	@Subscribe public void onPiecePlaced(final PiecePlacedMessage message) {
		String playerId = message.getPlayerId();
		String cardId = message.getCardInstanceId();
		
		Game game = CurrentUserInfo.getInstance().getGame();
		
		if (game != null) {

			if (card.getId().equals(cardId)) {
				card.getPieceLeftToPlace().poll();
				updateFiguresNumberText();
				if (card.getPieceLeftToPlace().size() == 0) {
					addAllPiecesButton.setVisible(false);
				} else if (StringUtils.equals(playerId, CurrentUserInfo.getInstance().getPlayerId())) {
					new AddPieceAction(card.getPieceLeftToPlace().peek(), card).actionPerformed(null);
				}
			}
		}
	}
	
	@Subscribe public void onPieceRemoved(final PieceRemovedMessage message) {
		String pieceId = message.getPieceId();

		PieceInstance piece = card.getPiece(pieceId);
		if (piece != null) {
			if (piece != null && piece.getCard() != null) {
				String cardId = piece.getCard().getId();
				if (card.getId().equals(cardId)) {
					card.getPieceLeftToPlace().add(piece.getModelId());
					if (card.getPieceLeftToPlace().size() > 0) {
						addAllPiecesButton.setVisible(true);
					}
					updateFiguresNumberText();
				}
			}
		}
	}
	
	public void openMarkersMenu() {
		Element popup = nifty.createPopup("niftyPopupMenu");
		@SuppressWarnings("unchecked")
		Menu<CardInstance> popupMenu = popup.findNiftyControl("#menu", Menu.class);
		popupMenu.setWidth(new SizeValue("250px")); // this is required and is not happening automatically!
		
		
//		Collection<MarkerDefinition> markerTypes = MarkerService.getInstance().getMarkersListForCard();
//		
//		for (final MarkerDefinition markerType : markerTypes) {
//			
//			switch(markerType.getType()) {
//			case STACKABLE:
//				final ImageIcon icon = new ImageIcon(markerType.getImage().getAbsolutePath());
//				JMenu addStackableMarkerMenu = new JMenu("add/remove " + markerType.getName() + " to this card");
//				addStackableMarkerMenu.setIcon(icon);
//				for (int i = 1; i <= 10; i++) {
//					JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(markerType, card, i));
//					addStackableMarkerMenu.add(numberItem);
//				}
//				for (int i = -1; i >= -10; i--) {
//					JMenuItem numberItem = new JMenuItem(new AddStackableMarkerToCardAction(markerType, card, i));
//					addStackableMarkerMenu.add(numberItem);
//				}
//				add(addStackableMarkerMenu);
//				break;
//			case REVEALABLE:
//				add(new JMenuItem(new AddMarkerToCardAction(markerType, ((RevealableMarkerDefinition)markerType).getHiddenMarkerDefinition(), card)));
//				break;
//			case NORMAL:
//				add(new JMenuItem(new AddMarkerToCardAction(markerType, null, card)));
//				break;
//			case HIDDEN:
//				break;
//			}
//		}
//		
//		for (final MarkerDefinition markerType : markerTypes) {
//			if (markerType instanceof HiddenMarkerDefinition) {
//				JMenuItem removeMarkersItem = new JMenuItem(new RemoveAllMarkersOfTypeForPlayerdAction(markerType));
//				add(removeMarkersItem);
//			}
//		}
//		
//		Game game = CurrentUserInfo.getInstance().getGame();
//		if (game != null) {
//			if (game.getPlayers().size() > 1) {
//				final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/share.png"));
//				JMenu giveCardToNewOwnerMenu = new JMenu("give this card to another player :");
//				giveCardToNewOwnerMenu.setIcon(icon);
//				for (Player player : game.getPlayers()) {
//					if (player.getArmy() != null && !player.getArmy().hasCard(card.getId())) {
//						JMenuItem giveCardToPlayerItem = new JMenuItem(new ChangeCardOwnerAction(card, player));
//						giveCardToPlayerItem.setText(player.getDisplayName());
//						giveCardToNewOwnerMenu.add(giveCardToPlayerItem);
//					}
//				}
//				add(giveCardToNewOwnerMenu);
//			}
//		}
		
		popupMenu.addMenuItem("MenuItem 1", "gui/images/chat/player.png", card);
		popupMenu.addMenuItemSeparator();
		popupMenu.addMenuItem("MenuItem 2", "gui/images/chat/observer.png", card);
		
		nifty.showPopup(screen, popup.getId(), null);
	}
}
