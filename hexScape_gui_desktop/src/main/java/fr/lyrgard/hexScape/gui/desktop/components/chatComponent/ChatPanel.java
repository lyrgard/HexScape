package fr.lyrgard.hexScape.gui.desktop.components.chatComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.CardCollection;
import fr.lyrgard.hexScape.model.DiceFace;
import fr.lyrgard.hexScape.model.DiceType;
import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.Player;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.marker.MarkerInstance;

public class ChatPanel extends JPanel{
	
	

	private static final long serialVersionUID = 1711726926387382729L;
	
	final private JTextPane textPane;
	
	private StyledDocument text;
	
	private final Style userNameStyle;
	
	private final Style textStyle;
	
	private final Style iconStyle;
	
	public ChatPanel() {
		
		setLayout(new BorderLayout());
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		
		Style defaultStyle = textPane.getStyle("default");
		userNameStyle = textPane.addStyle("userNameStyle", defaultStyle);
		textStyle = textPane.addStyle("textStyle", defaultStyle);
		iconStyle = textPane.addStyle("iconStyle", defaultStyle);
		
		StyleConstants.setBold(userNameStyle, true);
		StyleConstants.setAlignment(iconStyle, StyleConstants.ALIGN_CENTER);
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane, BorderLayout.CENTER);
		
		text = (StyledDocument)textPane.getDocument();

		final JTextField userInputField = new JTextField(20);
		userInputField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//We get the text from the textfield
				String fromUser = userInputField.getText();

				if (fromUser != null) {
					//We append the text from the user
					addLine(HexScapeCore.getInstance().getPlayer(), fromUser, false);
					//We reset our text field to "" each time the user presses Enter
					userInputField.setText("");
				}
			}
		});
		
		add(userInputField, BorderLayout.PAGE_END);
		
		Dimension dim = new Dimension(200, 400);
		setPreferredSize(dim);

		HexScapeCore.getInstance().getEventBus().register(this);
	}
	
	private void addLine(Player player, String line, boolean action) {
		try {
			if (player == null) {
				StyleConstants.setForeground(userNameStyle, Color.black);
			} else {
				StyleConstants.setForeground(userNameStyle, player.getColor());
			}
			if (action) {
				text.insertString(text.getEndPosition().getOffset(), "* " + line + " *\n", userNameStyle);
			} else {
				text.insertString(text.getEndPosition().getOffset(), player.getName() + " : ", userNameStyle);
				text.insertString(text.getEndPosition().getOffset(), line + "\n ", textStyle);
			}
			//The pane auto-scrolls with each new response added
			textPane.setCaretPosition(text.getEndPosition().getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void addDiceRoll(Player player, DiceType type, List<DiceFace> result) {
		try {
			StyleConstants.setForeground(userNameStyle, player.getColor());
			String line = player.getName() + " rolled " + result.size() + " " + type.getName() + " :\n";
			text.insertString(text.getEndPosition().getOffset(), line, userNameStyle);
			for (DiceFace face : result) {
				ImageIcon icon = new ImageIcon(face.getImage().getAbsolutePath());
			    StyleConstants.setIcon(iconStyle, icon);
				text.insertString(text.getEndPosition().getOffset()," ", iconStyle);
				text.insertString(text.getEndPosition().getOffset()," ", textStyle);
			}
			text.insertString(text.getEndPosition().getOffset(), "\n", userNameStyle);
			//The pane auto-scrolls with each new response added
			textPane.setCaretPosition(text.getEndPosition().getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	@Subscribe public void onPieceAdded(PieceAddedEvent event) {
		Player player = event.getActingUser();
		MoveablePiece piece = event.getPiece();
		addLine(player, player.getName() + " added " + piece.getModelName() + " to the map", true);
	}
	
	@Subscribe public void onPieceMoved(PieceMovedEvent event) {
		Player player = event.getActingUser();
		MoveablePiece piece = event.getPiece();
		addLine(player, player.getName() + " moved " + piece.getModelName(), true);
	}
	
	@Subscribe public void onPieceRemoved(PieceRemovedEvent event) {
		Player player = event.getActingUser();
		MoveablePiece piece = event.getPiece();
		addLine(player, player.getName() + " removed " + piece.getModelName() + " from the map", true);
	}
	
	@Subscribe public void onMapLoaded(MapLoadedEvent event) {
		Map map = event.getMap();
		addLine(null, "map " + map.getName() + " has been loaded", true);
	}
	
	@Subscribe public void onMapLoaded(ArmyLoadedEvent event) {
		CardCollection army = event.getArmy();
		Player player = event.getPlayer();
		addLine(player, "player " + player.getName() + " loaded army " + army.getName(), true);
	}
	
	@Subscribe public void onDiceRolled(DiceRolledEvent event) {
		DiceType diceType = event.getDiceType();
		List<DiceFace> result = event.getResult();
		Player player = event.getPlayer();
		addDiceRoll(player, diceType, result);
	}
	
	@Subscribe public void onMarkerOnCardChanged(MarkersOnCardChangedEvent event) {
		Player player = event.getPlayer();
		MarkerInstance marker = event.getMarker();
		Card card = event.getCard();
		int number = event.getNumber();
		if (marker == null && card.getMarkers().isEmpty()) {
			addLine(player, "player " + player.getName() + " removed all markers from " + card.getName(), true);
		} else if (marker != null) {
			if (number < 0) {
				addLine(player, "player " + player.getName() + " removed " + -number + " " + marker.getMarkerDefinition().getName() + " from " + card.getName(), true);
			} else {
				addLine(player, "player " + player.getName() + " added " + number + " " + marker.getMarkerDefinition().getName() + " to " + card.getName(), true);
			}
		}
		
	}
	
	@Subscribe public void onMarkerOnCardRevealed(MarkerRevealedOnCardEvent event) {
		Player player = event.getPlayer();
		MarkerInstance marker = event.getMarker();
		Card card = event.getCard();
		addLine(player, "player " + player.getName() + " revealed " + marker.getMarkerDefinition().getName() + " on " + card.getName(), true);
	}
}
