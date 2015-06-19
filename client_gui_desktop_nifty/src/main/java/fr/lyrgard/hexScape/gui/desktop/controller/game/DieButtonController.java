package fr.lyrgard.hexScape.gui.desktop.controller.game;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;

public class DieButtonController extends AbstractImageButtonController {
	
	public static final String DIE_TYPE_ID = "dieTypeId";
	public static final String NUMBER_OF_DICE = "numberOfDice";

	@Override
	public void onClick() {
		String diceTypeId = attributes.get(DIE_TYPE_ID);
		int number = attributes.getAsInteger(NUMBER_OF_DICE, 0);
		
		DiceType diceType = DiceService.getInstance().getDiceType(diceTypeId);
		
		if (diceType != null && number != 0) {
			ThrowDiceMessage message = new ThrowDiceMessage(CurrentUserInfo.getInstance().getPlayerId(), number, diceType.getId(), diceType.getFaces().size());
			CoreMessageBus.post(message);
		}
	}

}
