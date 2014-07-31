package fr.lyrgard.hexScape.gui.desktop.components.diceComponent;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.model.dice.DiceType;

public class DiceTypePanel extends JPanel {

	private static final long serialVersionUID = -595767426838310309L;

	public DiceTypePanel(final DiceType diceType) {
		super();
		
		setLayout(new GridLayout(0, 5, 3, 3));
		
		for (int i = 1; i <= diceType.getMaxNumberThrown(); i++) {
			final int number = i;
			JButton rollDiceButton = new JButton(Integer.toString(number));
			if (diceType.getBackgroundColor() != null) {
				rollDiceButton.setBackground(diceType.getBackgroundColor());
			}
			if (diceType.getForegroundColor() != null) {
				rollDiceButton.setForeground(diceType.getForegroundColor());
			}
			rollDiceButton.setFont(rollDiceButton.getFont().deriveFont(Font.BOLD));
			rollDiceButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					ThrowDiceMessage message = new ThrowDiceMessage(HexScapeCore.getInstance().getPlayerId(), number, diceType.getId(), diceType.getFaces().size());
					CoreMessageBus.post(message);
				}
			});
			add(rollDiceButton);
		}
	}
}
