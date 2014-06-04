package fr.lyrgard.hexScape.gui.desktop;

import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.common.eventbus.Subscribe;


import fr.lyrgard.hexScape.bus.MessageBus;
import fr.lyrgard.hexScape.gui.desktop.components.menuComponent.MenuBar;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.gui.desktop.view.game.GameView;
import fr.lyrgard.hexScape.gui.desktop.view.home.HomeView;
import fr.lyrgard.hexScape.gui.desktop.view.room.RoomView;
import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.message.WarningMessage;

public class HexScapeFrame extends JFrame {

	private static final long serialVersionUID = 7043232675085791117L;

	public static HexScapeFrame instance;

	public static HexScapeFrame getInstance() {
		return instance;
	}
	
	private final CardLayout layout = new CardLayout();
	
	private final Map<ViewEnum, AbstractView> viewsMap = new HashMap<ViewEnum, AbstractView>();

	public HexScapeFrame(final Canvas panel3d) {
		super("HexScape");

		
		instance = this;
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(layout);
		
		viewsMap.put(ViewEnum.HOME, new HomeView());
		viewsMap.put(ViewEnum.ROOM, new RoomView());
		viewsMap.put(ViewEnum.GAME, new GameView(panel3d));
		
		for (Entry<ViewEnum, AbstractView> entry : viewsMap.entrySet()) {
			add(entry.getValue(), entry.getKey().name());
		}
		showView(ViewEnum.HOME);
		
		
		
		setJMenuBar(new MenuBar());

		// Display Swing window including JME canvas!
		setExtendedState(JFrame.MAXIMIZED_BOTH); // aligns itself with windows task bar
		// set maximum screen   
		setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		setVisible(true);
		
		panel3d.transferFocusBackward();
		
		MessageBus.register(this);
	}
	
	public void showView(ViewEnum view) {
		layout.show(this.getContentPane(), view.name());
		viewsMap.get(view).refresh();
	}
	
	@Subscribe public void onConnectedToServer(ConnectedToServerMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				showView(ViewEnum.ROOM);
			}
		});
	}
	
	@Subscribe public void onMessage(final InfoMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String messageContent = message.getMessage();
				int messageType = JOptionPane.INFORMATION_MESSAGE;
				String title = "Message";
				JOptionPane.showMessageDialog(null, messageContent, title, messageType);
			}
		});
	}
	
	@Subscribe public void onMessage(final WarningMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String messageContent = message.getMessage();
				int messageType = JOptionPane.WARNING_MESSAGE;
				String title = "Warning";
				JOptionPane.showMessageDialog(null, messageContent, title, messageType);
			}
		});
	}
	
	@Subscribe public void onMessage(final ErrorMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				String messageContent = message.getMessage();
				int messageType = JOptionPane.ERROR_MESSAGE;
				String title = "Error";
				JOptionPane.showMessageDialog(null, messageContent, title, messageType);
			}
		});
	}
}
