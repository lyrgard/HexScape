package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.AddPieceAction;
import fr.lyrgard.hexScape.gui.desktop.message.DisplayCardDetailMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.card.CardType;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.marker.HiddenMarkerInstance;
import fr.lyrgard.hexScape.model.marker.MarkerDefinition;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.marker.StackableMarkerInstance;
import fr.lyrgard.hexScape.model.piece.PieceInstance;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.CardService;
import fr.lyrgard.hexScape.service.MarkerService;

public class ArmyCardPanelController implements Controller {
	private static final String CARD_NAME_TEXT = "#cardNameText";
	private static final String MARKERS_PANEL = "#markersPanel";
	private static final String FIGURES_NUMBER_TEXT = "#figuresNumberText";
	private static final String ADD_ALL_PIECES_BUTTON = "#putOnMapButton";
	
	private Nifty nifty;
	private Screen screen;
	
	private CardInstance card;
	private CardType cardType;
	private Player owner;
	
	private Element addAllPiecesButton;
	private Element figuresNumberText;
	private Element markersPanel;

	@Override
	public void bind(Nifty nifty, Screen screen, Element element, Properties properties, Attributes attributes) {
		this.nifty = nifty;
		this.screen = screen;
		String playerId = attributes.get(GameProperties.PLAYER_ID);
		String cardId = attributes.get(GameProperties.CARD_ID);
		
		Player player = CurrentUserInfo.getInstance().getGame().getPlayer(playerId);
		
		card = player.getArmy().getCard(cardId);
		cardType = CardService.getInstance().getCardInventory().getCardTypesById().get(card.getCardTypeId());

		owner = CurrentUserInfo.getInstance().getGame().getCardOwner(card.getId());
			
		Element cardNameText = element.findElementByName(CARD_NAME_TEXT);
		cardNameText.getRenderer(TextRenderer.class).setText(cardType.getName());
		
		figuresNumberText = element.findElementByName(FIGURES_NUMBER_TEXT);
		updateFiguresNumberText();
		
		markersPanel = element.findElementByName(MARKERS_PANEL);
		
		
		addAllPiecesButton = element.findElementByName(ADD_ALL_PIECES_BUTTON);
		PlaceAllPiecesButtonController addAllPiecesButtonController = addAllPiecesButton.getControl(PlaceAllPiecesButtonController.class);
		addAllPiecesButtonController.setCard(card);
		
		updateMarkers();
		
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
	
	public void updateMarkers() {
		for (Element action : markersPanel.getElements()) {
			action.markForRemoval();
		}
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				
				for (final MarkerInstance marker : card.getMarkers()) {
					final MarkerDefinition markerType = MarkerService.getInstance().getMarkersByIds().get(marker.getMarkerDefinitionId());
					
					new PanelBuilder() {{
						childLayoutHorizontal();
						interactOnClick("showDetail()");
						
						if (marker instanceof StackableMarkerInstance) {
							panel(new PanelBuilder() {{
								valign(VAlign.Center);
								childLayoutCenter();
								image(new ImageBuilder() {{
									filename("gui/images/textBackground.png");
									imageMode("resize:2,1,2,2,2,1,2,1,2,1,2,2");
									width("15px");
									height("20px");
								}});
								text(new TextBuilder() {{
									text(String.valueOf(((StackableMarkerInstance)marker).getNumber()));
									style("uiLabel16");
									textHAlignCenter();
									textVAlignCenter();
								}});
							}});
							
						}
						image(new ImageBuilder() {{
							filename(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(markerType.getImage().toURI()).getPath());
							valign(VAlign.Center);
							
							if (marker instanceof HiddenMarkerInstance && owner.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
								onHoverEffect(new HoverEffectBuilder("changeImage") {{
									String hiddenMarkerTypeId = ((HiddenMarkerInstance)marker).getHiddenMarkerDefinitionId();
									final MarkerDefinition hiddenMarkerType = MarkerService.getInstance().getMarkersByIds().get(hiddenMarkerTypeId);
									effectParameter("active", HexScapeCore.APP_DATA_FOLDER.toURI().relativize(hiddenMarkerType.getImage().toURI()).getPath());
									effectParameter("inactive", HexScapeCore.APP_DATA_FOLDER.toURI().relativize(markerType.getImage().toURI()).getPath());
								}});
								visibleToMouse(true);
								interactOnClick("showDetail()");
							} else {
								visibleToMouse(false);
							}
						}});
					}}.build(nifty, screen, markersPanel);
					
				}
				return null;
			}
		});
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
	
	public void showDetail() {
		GuiMessageBus.post(new DisplayCardDetailMessage(card));
	}
	
	@Subscribe public void onMarkerPlaced(MarkerPlacedMessage message) {
		String cardId = message.getCardId();
		
		if (card != null && card.getId().equals(cardId)) {
			updateMarkers();
		}
	}
	
	@Subscribe public void onMarkerRevealed(MarkerRevealedMessage message) {
		String cardId = message.getCardId();
		
		if (card != null && card.getId().equals(cardId)) {
			updateMarkers();
		}
	}
	
	@Subscribe public void onMarkerRemoved(MarkerRemovedMessage message) {
		String cardId = message.getCardId();
		
		if (card != null && card.getId().equals(cardId)) {
			updateMarkers();
		}
	}
}
