package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.gui.desktop.message.ChangeDieTypeSelectedMessage;

public class DiceTypeButtonController extends AbstractImageButtonController {
	
	public static final String DIE_TYPE_ID = "dieTypeId";
	public static final String NUMBER_OF_DICE = "numberOfDice";

	@Override
	public void onClick() {
		String diceTypeId = attributes.get(DIE_TYPE_ID);
		
		GuiMessageBus.post(new ChangeDieTypeSelectedMessage(diceTypeId));
	}

}
