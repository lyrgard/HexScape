package fr.lyrgard.hexScape.gui.desktop.view.home;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fr.lyrgard.hexScape.gui.desktop.action.ConnectToServerAction;
import fr.lyrgard.hexScape.gui.desktop.action.StartNewGameAction;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;

public class HomeView extends AbstractView {

	private static final long serialVersionUID = 7669212340835857265L;

	public HomeView() {
		JButton soloGame = new JButton(new StartNewGameAction(false));
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
		multiplayer.setEnabled(false);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
