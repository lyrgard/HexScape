package fr.lyrgard.hexScape.gui.desktop.view.home;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.concurrent.Callable;



import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.ConnectToServerAction;
import fr.lyrgard.hexScape.gui.desktop.action.JoinGameAction;
import fr.lyrgard.hexScape.gui.desktop.action.OpenConfigDialogAction;
import fr.lyrgard.hexScape.gui.desktop.action.OpenNewGameDialogAction;
//import fr.lyrgard.hexScape.gui.desktop.action.ConnectToServerAction;
//import fr.lyrgard.hexScape.gui.desktop.action.OpenConfigDialogAction;
//import fr.lyrgard.hexScape.gui.desktop.action.OpenNewGameDialogAction;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.TitleScreenSprite.Type;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.TitleScreenButtonClicked;

public class HomeView extends AbstractView {

	private static final long serialVersionUID = 7669212340835857265L;

	public HomeView() {

		setLayout(new BorderLayout());
		
		GuiMessageBus.register(this);
	}

	@Override
	public void refresh() {
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeCore.getInstance().getHexScapeJme3Application().displayTitleScreen();
				return null;
			}
		});
		Component view3d = HexScapeFrame.getInstance().getView3d().getComponent();
		//view3d.setPreferredSize(new Dimension(UNDEFINED_CONDITION, UNDEFINED_CONDITION));
		add(view3d, BorderLayout.CENTER);
		System.out.println("HOME VIEW TAKES 3D");
	}

	@Subscribe public void onGameCreatedSolo(GameCreatedMessage message) {
		if (!HexScapeCore.getInstance().isOnline()) {
			String gameId = message.getGame().getId();
			JoinGameAction action = new JoinGameAction(gameId);
			action.actionPerformed(null);
		}
	}
	
	@Subscribe public void onGameJoinSolo(GameJoinedMessage message) {
		if (!HexScapeCore.getInstance().isOnline()) {
			
			StartGameMessage resultMessage = new StartGameMessage(CurrentUserInfo.getInstance().getPlayerId(), message.getGame().getId());
			CoreMessageBus.post(resultMessage);
		}
	}

	@Subscribe public void onTitleScreenButtonClicked(TitleScreenButtonClicked message) {
		Type type = message.getType();
		switch (type) {
		case SOLO:
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					new OpenNewGameDialogAction(getTopLevelAncestor()).actionPerformed(null);
				}
			});
			break;
		case MULTIPLAYER:
			new ConnectToServerAction().actionPerformed(null);
			break;
		case CONFIG:
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					new OpenConfigDialogAction(getTopLevelAncestor()).actionPerformed(null);
				}
			});
			break;
		default:
		}
	}
}
