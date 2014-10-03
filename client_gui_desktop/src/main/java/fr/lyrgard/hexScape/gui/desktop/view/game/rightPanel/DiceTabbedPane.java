package fr.lyrgard.hexScape.gui.desktop.view.game.rightPanel;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.DiceDefinitionReloadedMessage;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;

public class DiceTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = -5228434117122831373L;

	public DiceTabbedPane() {
		loadDiceDefinition();
		GuiMessageBus.register(this);
	}
	
	private void loadDiceDefinition() {
		removeAll();
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
	
	@Subscribe public void onDiceDefinitionReloaded(DiceDefinitionReloadedMessage message) {
		loadDiceDefinition();
	}
}
