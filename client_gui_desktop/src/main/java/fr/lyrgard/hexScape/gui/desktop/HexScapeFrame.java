package fr.lyrgard.hexScape.gui.desktop;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.gui.desktop.view.AbstractView;
import fr.lyrgard.hexScape.gui.desktop.view.common.View3d;
import fr.lyrgard.hexScape.gui.desktop.view.game.GameView;
import fr.lyrgard.hexScape.gui.desktop.view.home.HomeView;
import fr.lyrgard.hexScape.gui.desktop.view.room.RoomView;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.message.WarningMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class HexScapeFrame extends JFrame {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HexScapeFrame.class);

	private static final long serialVersionUID = 7043232675085791117L;

	private static HexScapeFrame instance;

	public static HexScapeFrame getInstance() {
		return instance;
	}

	private View3d view3d;
	
	private final CardLayout layout = new CardLayout();

	private final Map<ViewEnum, AbstractView> viewsMap = new HashMap<ViewEnum, AbstractView>();

	public HexScapeFrame(final View3d view3d) {
		super("HexScape");
		this.view3d = view3d;


		instance = this;

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while loading Look And Feels Nimbus", e);
		}
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (HexScapeCore.getInstance().isOnline()) {
					DisconnectFromServerMessage message = new DisconnectFromServerMessage();
	        		CoreMessageBus.post(message);
				}
				HexScapeCore.getInstance().getHexScapeJme3Application().stop(true);
				System.exit(0);
			}
		});
		
		setLayout(layout);

		viewsMap.put(ViewEnum.HOME, new HomeView());
		viewsMap.put(ViewEnum.ROOM, new RoomView());
		viewsMap.put(ViewEnum.GAME, new GameView(view3d));

		for (Entry<ViewEnum, AbstractView> entry : viewsMap.entrySet()) {
			add(entry.getValue(), entry.getKey().name());
		}
		showView(ViewEnum.HOME);



		//setJMenuBar(new MenuBar());

		setExtendedState(JFrame.MAXIMIZED_BOTH); // aligns itself with windows task bar
		// set maximum screen   
		setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		setVisible(true);

		GuiMessageBus.register(this);
	}

	public void showView(ViewEnum view) {
		layout.show(getContentPane(), view.name());
		viewsMap.get(view).refresh();
		validate();
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
	}


	@Subscribe public void onDisconnectedFromServer(DisconnectedFromServerMessage message) {
		if (message.getUserId().equals(CurrentUserInfo.getInstance().getId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					showView(ViewEnum.HOME);
				}
			});
		}
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
				LOGGER.warn(messageContent);
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
				LOGGER.error(messageContent);
				JOptionPane.showMessageDialog(null, messageContent, title, messageType);
			}
		});
	}

	public View3d getView3d() {
		return view3d;
	}

}
