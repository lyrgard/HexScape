package fr.lyrgard.hexScape.gui.desktop.view.home;


import javax.swing.JButton;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.ConnectToServerAction;
import fr.lyrgard.hexScape.gui.desktop.action.OpenConfigDialogAction;
import fr.lyrgard.hexScape.gui.desktop.action.OpenNewGameDialogAction;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.DisplayMapMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;

public class HomeView extends AbstractView {

	private static final long serialVersionUID = 7669212340835857265L;

	public HomeView() {
		JButton soloGame = new JButton(new OpenNewGameDialogAction(false, getTopLevelAncestor()));
		soloGame.setText("Solo game");
//		soloGame.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				HexScapeFrame.getInstance().showView(ViewEnum.GAME);
//			}
//		});
		add(soloGame);
		
		JButton multiplayer = new JButton(new ConnectToServerAction());
//		multiplayer.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				ConnectToServerAction action = new ConnectToServerAction();
//				action.actionPerformed(e);
//			}
//		});
		add(multiplayer);
		
		
		JButton configButton = new JButton(new OpenConfigDialogAction(getTopLevelAncestor()));
		add(configButton);
		
		//multiplayer.setEnabled(false);
		GuiMessageBus.register(this);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		if (!HexScapeCore.getInstance().isOnline()) {
			String gameId = message.getGame().getId();
			StartGameMessage resultMessage = new StartGameMessage(HexScapeCore.getInstance().getPlayerId(), gameId);
			CoreMessageBus.post(resultMessage);
		}
	}
}
