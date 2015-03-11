package fr.lyrgard.hexScape.listener;

import java.io.File;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CardInstanceChangedOwnerMessage;
import fr.lyrgard.hexScape.message.ChangeCardInstanceOwnerMessage;
import fr.lyrgard.hexScape.message.LoadArmyMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.ArmyService;
import fr.lyrgard.hexscape.client.network.ClientNetwork;

public class ArmyMessageListener extends AbstractMessageListener {

	private static ArmyMessageListener instance;

	public static void start() {
		if (instance == null) {
			instance = new ArmyMessageListener();
			CoreMessageBus.register(instance);
		}
	}

	private ArmyMessageListener() {
	}

	@Subscribe public void onLoadArmyMessage(LoadArmyMessage message) {
		File armyFile = message.getArmyFile();
		String playerId = message.getPlayerId();

		Army army = ArmyService.getInstance().loadArmy(playerId, armyFile);

		if (army != null) {
			sendMessage(new ArmyLoadedMessage(playerId, army));
		}
	}

	@Subscribe public void onArmyLoaded(ArmyLoadedMessage message) {
		String playerId = message.getPlayerId();
		Army army = message.getArmy();

		if (CurrentUserInfo.getInstance().getGameId() != null) {
			Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());

			if (game != null) {
				Player player = game.getPlayer(playerId);
				if (player != null) {
					player.setArmy(army);
					GuiMessageBus.post(message);
				}
			}
		}
	}

	@Subscribe public void onChangeCardInstanceOwner(ChangeCardInstanceOwnerMessage message) {
		if (HexScapeCore.getInstance().isOnline()) {
			ClientNetwork.getInstance().send(message);
		} else {
			if (CurrentUserInfo.getInstance().getGameId() != null) {
				Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());
				
				String cardId = message.getCardId();
				String newOwnerId = message.getNewOwnerId();
				
				Player currentOwner = game.getCardOwner(cardId);
				Player newOwner = game.getPlayer(newOwnerId);
				
				if (currentOwner != null && newOwner != null && newOwner.getArmy() != null) {
					String newCardId = newOwnerId + "-" + newOwner.getArmy().getCards().size();
					
					CardInstanceChangedOwnerMessage resultMessage = new CardInstanceChangedOwnerMessage(cardId, currentOwner.getId(), newCardId, newOwnerId);
					
					CoreMessageBus.post(resultMessage);
				}
			}
		}
	}

	@Subscribe public void onCardInstanceChangedOwner(CardInstanceChangedOwnerMessage message) {
		String oldCardId = message.getOldCardId();
		String newCardId = message.getNewCardId();
		String newOwnerId = message.getNewOwnerId();

		if (CurrentUserInfo.getInstance().getGameId() != null) {
			Game game = CurrentUserInfo.getInstance().getGame();

			Player currentOwner = game.getCardOwner(oldCardId);
			Player newOwner = game.getPlayer(newOwnerId);

			CardInstance card = game.getCard(oldCardId);

			if (currentOwner != null && newOwner != null && newOwner.getArmy() != null && card != null) {
				currentOwner.getArmy().getCards().remove(card);
				card.setId(newCardId);
				newOwner.getArmy().addCard(card);
				
				GuiMessageBus.post(message);
			}
		}

	}

}
