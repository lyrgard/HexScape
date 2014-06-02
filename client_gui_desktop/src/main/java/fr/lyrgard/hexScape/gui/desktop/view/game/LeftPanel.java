package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ArmyLoadedEvent;
import fr.lyrgard.hexScape.event.DiceRolledEvent;
import fr.lyrgard.hexScape.event.MapLoadedEvent;
import fr.lyrgard.hexScape.event.marker.MarkerRevealedOnCardEvent;
import fr.lyrgard.hexScape.event.marker.MarkersOnCardChangedEvent;
import fr.lyrgard.hexScape.event.piece.PieceAddedEvent;
import fr.lyrgard.hexScape.event.piece.PieceMovedEvent;
import fr.lyrgard.hexScape.event.piece.PieceRemovedEvent;
import fr.lyrgard.hexScape.gui.desktop.components.chatComponent.ChatPanel;
import fr.lyrgard.hexScape.gui.desktop.components.diceComponent.DiceTabbedPane;
import fr.lyrgard.hexScape.model.ChatTypeEnum;
import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.card.Card;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class LeftPanel extends JPanel {
	
	private static final long serialVersionUID = -8890928527786697273L;

	private ChatPanel chatPanel;
	
	public LeftPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		chatPanel = new ChatPanel(ChatTypeEnum.GAME_CHAT);
		add(chatPanel);
		add(new DiceTabbedPane());
		
		HexScapeCore.getInstance().getEventBus().register(this);
	}
	
	@Subscribe public void onPieceAdded(PieceAddedEvent event) {
		Player player = event.getActingUser();
		MoveablePiece piece = event.getPiece();
		chatPanel.addPlayerAction(player, player.getName() + " added " + piece.getModelName() + " to the map");
	}
	
	@Subscribe public void onPieceMoved(PieceMovedEvent event) {
		Player player = event.getActingUser();
		MoveablePiece piece = event.getPiece();
		chatPanel.addPlayerAction(player, player.getName() + " moved " + piece.getModelName());
	}
	
	@Subscribe public void onPieceRemoved(PieceRemovedEvent event) {
		Player player = event.getActingUser();
		MoveablePiece piece = event.getPiece();
		chatPanel.addPlayerAction(player, player.getName() + " removed " + piece.getModelName() + " from the map");
	}
	
	@Subscribe public void onMapLoaded(MapLoadedEvent event) {
		Map map = event.getMap();
		chatPanel.addAction("map " + map.getName() + " has been loaded");
	}
	
	@Subscribe public void onArmyLoaded(ArmyLoadedEvent event) {
		CardCollection army = event.getArmy();
		Player player = event.getPlayer();
		chatPanel.addPlayerAction(player, "player " + player.getName() + " loaded army " + army.getName());
	}
	
	@Subscribe public void onDiceRolled(DiceRolledEvent event) {
		DiceType diceType = event.getDiceType();
		List<DiceFace> result = event.getResult();
		Player player = event.getPlayer();
		chatPanel.addDiceRoll(player, diceType, result);
	}
	
	@Subscribe public void onMarkerOnCardChanged(MarkersOnCardChangedEvent event) {
		Player player = event.getPlayer();
		MarkerInstance marker = event.getMarker();
		Card card = event.getCard();
		int number = event.getNumber();
		if (marker == null && card.getMarkers().isEmpty()) {
			chatPanel.addPlayerAction(player, "player " + player.getName() + " removed all markers from " + card.getName());
		} else if (marker != null) {
			if (number < 0) {
				chatPanel.addPlayerAction(player, "player " + player.getName() + " removed " + -number + " " + marker.getMarkerDefinition().getName() + " from " + card.getName());
			} else {
				chatPanel.addPlayerAction(player, "player " + player.getName() + " added " + number + " " + marker.getMarkerDefinition().getName() + " to " + card.getName());
			}
		}
		
	}
	
	@Subscribe public void onMarkerOnCardRevealed(MarkerRevealedOnCardEvent event) {
		Player player = event.getPlayer();
		MarkerInstance marker = event.getMarker();
		Card card = event.getCard();
		chatPanel.addPlayerAction(player, "player " + player.getName() + " revealed " + marker.getMarkerDefinition().getName() + " on " + card.getName());
	}
}
