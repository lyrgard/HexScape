package fr.lyrgard.hexScape.gui.desktop.components.diceComponent;

import java.awt.Color;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;

public class DiceTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = -5228434117122831373L;

	public DiceTabbedPane() {
		Collection<DiceType> diceTypes = DiceService.getInstance().getDiceTypes();
		int i = 0;
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
			i++;
		}
		
//		addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				int selectedIndex = DiceTabbedPane.this.getSelectedIndex();
//				for (int i = 0; i < DiceTabbedPane.this.getComponentCount(); i++) {
//					if (i == selectedIndex) {
//						DiceTabbedPane.this.getTabComponentAt(1).setBackground(Color.BLACK);
//					} else {
//						DiceTabbedPane.this.getTabComponentAt(1).setBackground(Color.WHITE);
//					}
//				}
//			}
//		});
	}
	
	
}
