package fr.lyrgard.hexScape.gui.desktop.view.game.rightPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.components.GradientButton;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.dice.DiceType;

public class DiceTypePanel extends JPanel {

	private static final long serialVersionUID = -595767426838310309L;

	public DiceTypePanel(final DiceType diceType) {
		super();
		
		setLayout(new GridLayout(0, 5, 3, 3));
		
		for (int i = 1; i <= diceType.getMaxNumberThrown(); i++) {
			final int number = i;
			
			Color color1 = Color.WHITE;
			Color color2 = Color.GRAY;
			if (diceType.getBackgroundColor() != null) {
				color2 = diceType.getBackgroundColor();
			}
			
			JButton rollDiceButton = new GradientButton(Integer.toString(number), color1, color2);
			if (diceType.getForegroundColor() != null) {
				rollDiceButton.setForeground(diceType.getForegroundColor());
			}
			rollDiceButton.setFont(rollDiceButton.getFont().deriveFont(Font.BOLD));
			rollDiceButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					ThrowDiceMessage message = new ThrowDiceMessage(CurrentUserInfo.getInstance().getPlayerId(), number, diceType.getId(), diceType.getFaces().size());
					CoreMessageBus.post(message);
				}
			});
			add(rollDiceButton);
		}
	}
}
