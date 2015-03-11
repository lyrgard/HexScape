package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ChangeCardInstanceOwnerMessage;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.player.Player;

public class ChangeCardOwnerAction extends AbstractAction {

	private static final long serialVersionUID = -5678423869384227763L;

	private CardInstance card;
	
	private Player newOwner;
	
	public ChangeCardOwnerAction(CardInstance card, Player newOwner) {
		super("Give " + card.getId() + " to player " + newOwner.getDisplayName());
		this.card = card;
		this.newOwner = newOwner;
	}

	public void actionPerformed(ActionEvent e) {
		CoreMessageBus.post(new ChangeCardInstanceOwnerMessage(card.getId(), newOwner.getId()));
	}
}
