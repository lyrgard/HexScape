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

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.message.PostMessageMessage;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.model.player.Player;

public class ChatPanel extends JPanel{
	
	private static final long serialVersionUID = 1711726926387382729L;
	
	final private JTextPane textPane;
	
	private StyledDocument text;
	
	private final Style userNameStyle;
	
	private final Style actionStyle;
	
	private final Style textStyle;
	
	private final Style iconStyle;
	
	public ChatPanel(final String roomId, final String gameId) {
		
		setLayout(new BorderLayout());
		
		textPane = new JTextPane();
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
					PostMessageMessage message = new PostMessageMessage(HexScapeCore.getInstance().getPlayerId(), messageContent, roomId, gameId);
					MessageBus.post(message);
					//We reset our text field to "" each time the user presses Enter
					userInputField.setText("");
				}
			}
		});
		
		add(userInputField, BorderLayout.PAGE_END);
		
		Dimension dim = new Dimension(200, 400);
		setPreferredSize(dim);

		MessageBus.register(this);
	}
	
	public void addMessage(Player player, String line) {
		try {
			StyleConstants.setForeground(userNameStyle, player.getColor());
			text.insertString(text.getEndPosition().getOffset(), player.getName() + " : ", userNameStyle);
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
			StyleConstants.setForeground(actionStyle, player.getColor());
			text.insertString(text.getEndPosition().getOffset(), "* " + line + " *\n", actionStyle);
			//The pane auto-scrolls with each new response added
			textPane.setCaretPosition(text.getEndPosition().getOffset() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void addDiceRoll(Player player, DiceType type, List<DiceFace> result) {
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
	
}
