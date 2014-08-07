package fr.lyrgard.hexScape.gui.desktop.view.home;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.concurrent.Callable;


import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.ConnectToServerAction;
import fr.lyrgard.hexScape.gui.desktop.action.OpenConfigDialogAction;
import fr.lyrgard.hexScape.gui.desktop.action.OpenNewGameDialogAction;
import fr.lyrgard.hexScape.gui.desktop.components.game.View3d;
//import fr.lyrgard.hexScape.gui.desktop.action.ConnectToServerAction;
//import fr.lyrgard.hexScape.gui.desktop.action.OpenConfigDialogAction;
//import fr.lyrgard.hexScape.gui.desktop.action.OpenNewGameDialogAction;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.model.TitleScreenSprite.Type;
import fr.lyrgard.hexScape.model.TitleScreenButtonClicked;

public class HomeView extends AbstractView {

	private static final long serialVersionUID = 7669212340835857265L;

	public HomeView() {

		setLayout(new BorderLayout());
		//		//setLayout(null);
		//		
		//		JLayeredPane layeredPane = new JLayeredPane();
		//		layeredPane.setBorder(new LineBorder(Color.BLACK));
		//		add(layeredPane);
		//		
		//		layeredPane.setBounds(0, 0, 800, 640);
		//		
		//		ImageIcon backgroundImage = new ImageIcon(HexScapeFrame.class.getResource("/gui/images/background.png"));
		//		JLabel background = new JLabel(backgroundImage);
		//		background.setBounds(0,0,800,640);
		//		layeredPane.add(background, new Integer(10));
		//		
		//		
		//		final ImageIcon configImage = new ImageIcon(HexScapeFrame.class.getResource("/gui/images/config.png"));
		//		final ImageIcon configImageHover = new ImageIcon(configImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		//		final JButton config = new JButton(new OpenConfigDialogAction(getTopLevelAncestor()));
		//		config.setBorderPainted(false);
		//		config.setOpaque(false);
		//		config.setContentAreaFilled(false);
		//		config.setCursor(new Cursor(Cursor.HAND_CURSOR));
		//
		//		config.setText("");
		//		config.setIcon(configImage);
		//		config.addMouseListener(new MouseAdapter() {
		//
		//			@Override
		//			public void mouseEntered(MouseEvent e) {
		//				config.setIcon(configImageHover);
		//				super.mouseEntered(e);
		//			}
		//
		//			@Override
		//			public void mouseExited(MouseEvent e) {
		//				config.setIcon(configImage);
		//				super.mouseExited(e);
		//			}
		//			
		//		});
		//		
		//		config.setBounds(585,484,150,150);
		//		layeredPane.add(config, new Integer(11));
		//		
		//		
		//		
		//		JButton soloGame = new JButton(new OpenNewGameDialogAction(false, getTopLevelAncestor()));
		//		soloGame.setText("Solo game");
		////		soloGame.addActionListener(new ActionListener() {
		////			
		////			public void actionPerformed(ActionEvent e) {
		////				HexScapeFrame.getInstance().showView(ViewEnum.GAME);
		////			}
		////		});
		//		add(soloGame);
		//		
		//		JButton multiplayer = new JButton(new ConnectToServerAction());
		////		multiplayer.addActionListener(new ActionListener() {
		////			
		////			public void actionPerformed(ActionEvent e) {
		////				ConnectToServerAction action = new ConnectToServerAction();
		////				action.actionPerformed(e);
		////			}
		////		});
		//		add(multiplayer);
		//		
		//		
		//		JButton configButton = new JButton(new OpenConfigDialogAction(getTopLevelAncestor()));
		//		add(configButton);

		//multiplayer.setEnabled(false);
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
		View3d view3d = HexScapeFrame.getInstance().getView3d();
		view3d.setPreferredSize(new Dimension(UNDEFINED_CONDITION, UNDEFINED_CONDITION));
		add(view3d, BorderLayout.CENTER);
	}

	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		if (!HexScapeCore.getInstance().isOnline()) {
			String gameId = message.getGame().getId();
			StartGameMessage resultMessage = new StartGameMessage(HexScapeCore.getInstance().getPlayerId(), gameId);
			CoreMessageBus.post(resultMessage);
		}
	}

	@Subscribe public void onTitleScreenButtonClicked(TitleScreenButtonClicked message) {
		Type type = message.getType();
		switch (type) {
		case SOLO:
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					new OpenNewGameDialogAction(false, getTopLevelAncestor()).actionPerformed(null);
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
