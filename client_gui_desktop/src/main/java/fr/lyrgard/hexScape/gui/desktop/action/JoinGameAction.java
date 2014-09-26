package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.view.common.newGame.PlayerSelectedListener;
import fr.lyrgard.hexScape.gui.desktop.view.common.newGame.SelectPlayerDialog;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.Universe;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class JoinGameAction extends AbstractAction implements PlayerSelectedListener {

	private static final long serialVersionUID = -8793227823204727607L;

	private String gameId;
	
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/connect.png"));
	
	public JoinGameAction(String gameId) {
		super("Join game", icon);
		this.gameId = gameId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game game = Universe.getInstance().getGamesByGameIds().get(gameId);
		if (game != null) {
			Collection<Player> freePlayers = game.getFreePlayers();
			if (freePlayers.isEmpty()) {
				GuiMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getId(), "No places left in the game"));
				return;
			} else if (freePlayers.size() == 1){
				JoinGameMessage message = new JoinGameMessage(CurrentUserInfo.getInstance().getId(), gameId, freePlayers.iterator().next().getId());
				CoreMessageBus.post(message);
			} else {
				SelectPlayerDialog selectPlaceholderDialog = new SelectPlayerDialog(game, this);
				selectPlaceholderDialog.setModal(true);
				selectPlaceholderDialog.setVisible(true);
			}
			
		}
		
	}

	@Override
	public void playerSelected(String placeholderId) {
		JoinGameMessage message = new JoinGameMessage(CurrentUserInfo.getInstance().getId(), gameId, placeholderId);
		CoreMessageBus.post(message);
	}
}
