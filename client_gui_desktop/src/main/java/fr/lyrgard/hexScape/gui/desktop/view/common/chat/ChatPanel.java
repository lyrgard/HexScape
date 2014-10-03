package fr.lyrgard.hexScape.gui.desktop.view.common.chat;

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

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexScape.message.PostGameMessageMessage;
import fr.lyrgard.hexScape.message.PostRoomMessageMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.player.User;

public class ChatPanel extends JPanel{
	
	private static final long serialVersionUID = 1711726926387382729L;
	
	final private JTextPane textPane;
	
	private StyledDocument text;
	
	private final Style userNameStyle;
	
	private final Style actionStyle;
	
	private final Style textStyle;
	
	private final Style iconStyle;
	
	private String roomId;
	
	private String gameId;
	
	public ChatPanel(final String roomId, final String gameId) {
		this.roomId = roomId;
		this.gameId = gameId;
		
		setLayout(new BorderLayout());
		
		textPane = new WrapTextPane();
		textPane.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		
		Style defaultStyle = textPane.getStyle("default");
		userNameStyle = textPane.addStyle("userNameStyle", defaultStyle);
		textStyle = textPane.addStyle("textStyle", defaultStyle);
		iconStyle = textPane.addStyle("iconStyle", defaultStyle);
		actionStyle = textPane.addStyle("actionStyle", defaultStyle);
		
		StyleConstants.setBold(userNameStyle, true);
		StyleConstants.setAlignment(iconStyle, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(actionStyle, true);
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane, BorderLayout.CENTER);
		
		text = (StyledDocument)textPane.getDocument();

		final JTextField userInputField = new JTextField(20);
		userInputField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//We get the text from the textfield
				String messageContent = userInputField.getText();

				if (messageContent != null) {
					if (ChatPanel.this.gameId != null || ChatPanel.this.roomId != null) {
						AbstractMessage message = null;
						if (ChatPanel.this.gameId != null) {
							message = new PostGameMessageMessage(CurrentUserInfo.getInstance().getPlayerId(), messageContent, ChatPanel.this.gameId);
						} else {
							message = new PostRoomMessageMessage(CurrentUserInfo.getInstance().getId(), messageContent, ChatPanel.this.roomId);
						}

						CoreMessageBus.post(message);
						//We reset our text field to "" each time the user presses Enter
						userInputField.setText("");
					}
				}
			}
		});
		
		add(userInputField, BorderLayout.PAGE_END);
		
		Dimension dim = new Dimension(200, 400);
		setPreferredSize(dim);

		GuiMessageBus.register(this);
	}
	
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void addMessage(Player player, String line) {
		addMessage(player.getName(), player.getColor().getColor(), line);
	}
	
	public void addMessage(User user, String line) {
		addMessage(user.getName(), user.getColor().getColor(), line);
	}
	
	public void addMessage(String user, Color color, String line) {
		try {
			StyleConstants.setForeground(userNameStyle, color);
			text.insertString(text.getEndPosition().getOffset(), user + " : ", userNameStyle);
			text.insertString(text.getEndPosition().getOffset(), line + "\n", textStyle);
			//The pane auto-scrolls with each new response added
			textPane.setCaretPosition(text.getEndPosition().getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void addAction(String line) {
		try {
			StyleConstants.setForeground(actionStyle, Color.black);
			text.insertString(text.getEndPosition().getOffset(), "* " + line + " *\n", actionStyle);
			//The pane auto-scrolls with each new response added
			textPane.setCaretPosition(text.getEndPosition().getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void addPlayerAction(Player player, String line) {
		try {
			StyleConstants.setForeground(actionStyle, player.getColor().getColor());
			text.insertString(text.getEndPosition().getOffset(), "* " + line + " *\n", actionStyle);
			//The pane auto-scrolls with each new response added
			textPane.setCaretPosition(text.getEndPosition().getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void addDiceRoll(Player player, DiceType type, List<DiceFace> result) {
		try {
			StyleConstants.setForeground(userNameStyle, player.getColor().getColor());
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
	
	public void clearText() {
		textPane.setText("");
	}

	public String getRoomId() {
		return roomId;
	}

	public String getGameId() {
		return gameId;
	}	
	
}
