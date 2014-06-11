package fr.lyrgard.hexScape.listener;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.AbstractMessage;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public abstract class AbstractMessageListener {

	protected void sendMessage(AbstractMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			CoreMessageBus.post(message);
		}
	}
}
