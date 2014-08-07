package fr.lyrgard.hexScape.gui.desktop.components.diceComponent;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;

public class DiceTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = -5228434117122831373L;

	public DiceTabbedPane() {
		Collection<DiceType> diceTypes = DiceService.getInstance().getDiceTypes();
		for (DiceType diceType : diceTypes) {
			DiceTypePanel diceTypePanel = new DiceTypePanel(diceType);
			String title = null;
			ImageIcon icon = null;
			if (diceType.getIconFile() != null) {
				icon = new ImageIcon(diceType.getIconFile().getAbsolutePath());
			} else {
				title = diceType.getName();
			}
			addTab(title, icon, diceTypePanel, diceType.getName());
		}
		
	}
	
	
}
