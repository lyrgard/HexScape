package fr.lyrgard.hexScape.gui.desktop.dice;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.DiceRolledEvent;
import fr.lyrgard.hexScape.model.DiceFace;
import fr.lyrgard.hexScape.model.DiceType;

public class DiceTypePanel extends JPanel {

	private static final long serialVersionUID = -595767426838310309L;

	public DiceTypePanel(final DiceType diceType) {
		super();
		
		setLayout(new GridLayout(0, 5, 2, 2));
		
		for (int i = 1; i <= diceType.getMaxNumberThrown(); i++) {
			final int number = i;
			JButton rollDiceButton = new JButton(Integer.toString(number));
			rollDiceButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					List<DiceFace> result = HexScapeCore.getInstance().getDiceService().roll(number, diceType);
					HexScapeCore.getInstance().getEventBus().post(new DiceRolledEvent(diceType, result, HexScapeCore.getInstance().getPlayer()));
				}
			});
			add(rollDiceButton);
		}
	}
}
