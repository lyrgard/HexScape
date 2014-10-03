package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;


import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class ArmiesTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 7018446148678245684L;
	
	private Map<String, ArmyPanel> armyPanelByPlayerIds = new HashMap<String, ArmyPanel>();
	
	private Map<String, Integer> tabIndexByPlayerId = new HashMap<>();
	
	public ArmiesTabbedPane() {
		//addTab("Your army", yourArmyPanel);
		//setBorder(new LineBorder(Color.red, 2));
		GuiMessageBus.register(this);
	}
	
	public void empty() {
		removeAll();
		//yourArmyPanel.setArmy(null, null);
		//addTab("Your army", yourArmyPanel);
	}

	@Subscribe public void onArmyLoaded(final ArmyLoadedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String playerId = message.getPlayerId();
				Army army = message.getArmy();
				
				Game game = Universe.getInstance().getGamesByGameIds().get(CurrentUserInfo.getInstance().getGameId());

				if (game != null) {
					ArmyPanel armyPanel = armyPanelByPlayerIds.get(playerId);
					
					if (armyPanel != null) {
						armyPanel.setArmy(army);
					}
				}
			}
		});
	}
	
	@Subscribe public void onGameStart(GameStartedMessage message) {
		String gameId = message.getGameId();
		
		removeAll();
		armyPanelByPlayerIds.clear();
		tabIndexByPlayerId.clear();
		
		final Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		
		if (game != null && gameId.equals(CurrentUserInfo.getInstance().getGameId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					
					
					Player currentPlayer = game.getPlayer(CurrentUserInfo.getInstance().getPlayerId());
					if (currentPlayer != null) {
						ArmyPanel yourArmyPanel = new ArmyPanel(currentPlayer.getId());
						addTab("Your army (" + CurrentUserInfo.getInstance().getPlayer().getName() + ")", yourArmyPanel);
						yourArmyPanel.setArmy(currentPlayer.getArmy());
						armyPanelByPlayerIds.put(currentPlayer.getId(), yourArmyPanel);
						tabIndexByPlayerId.put(currentPlayer.getId(), 0);
					}
					
					int i = 1;
					for (Player player : game.getPlayers()) {
						if (!player.getId().equals(CurrentUserInfo.getInstance().getPlayerId())) {
							if (player != null) {
								ArmyPanel otherPlayerArmyPanel = new ArmyPanel(player.getId(), player.getArmy());
								addTab(player.getDisplayName(), otherPlayerArmyPanel);
								armyPanelByPlayerIds.put(player.getId(), otherPlayerArmyPanel);
								tabIndexByPlayerId.put(player.getId(), i);
								i++;
							}
						}
					}
				}
			});
		}
	}
	
	@Subscribe public void onGameJoined(GameJoinedMessage message) {
		Game game = message.getGame();
		String playerId = message.getPlayerId();
		
		if (game != null && game.getId().equals(CurrentUserInfo.getInstance().getGameId()) && game.isStarted()) {
			Player player = game.getPlayer(playerId);
			Integer tabIndex = tabIndexByPlayerId.get(playerId);
			if (tabIndex != null && player != null) {
				setTitleAt(tabIndex, player.getDisplayName());
			}
		}
	}
	
	@Subscribe public void onGameLeft(GameLeftMessage message) {
		String gameId = message.getGameId();
		String playerId = message.getPlayerId();
		
		if (CurrentUserInfo.getInstance().getPlayer() == null) {
			// current player is null == we left the game
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					empty();
				}
			});
		} else if (gameId.equals(CurrentUserInfo.getInstance().getGameId())){
			Game game = CurrentUserInfo.getInstance().getGame();
			Player player = game.getPlayer(playerId);
			Integer tabIndex = tabIndexByPlayerId.get(playerId);
			if (tabIndex != null && player != null) {
				setTitleAt(tabIndex, player.getName());
			}
		}
	} 

}
