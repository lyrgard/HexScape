package fr.lyrgard.hexScape.gui.desktop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;

public class HexScapeFrame extends JFrame {

	private static final long serialVersionUID = 7043232675085791117L;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HexScapeFrame.class);
	
	private JPanel canvasPanel;

	private static final HexScapeFrame INSTANCE = new HexScapeFrame();
	
	public static HexScapeFrame getInstance() {
		return INSTANCE;
	}
	
	private HexScapeFrame() {
		super("HexScape");
		
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
				HexScapeDesktopGui.getInstance().getNifty().exit();
				HexScapeCore.getInstance().getHexScapeJme3Application().stop();
				exit();
			}
		});
		
		canvasPanel = new JPanel (new BorderLayout());
		add(canvasPanel);
		
		
		Dimension preferredSize = new Dimension(1280, 768);
		setMinimumSize(preferredSize);

		setVisible(true);
	}
	
	public JPanel getCanvasContainer() {
		return canvasPanel;
	}



	public void setJmeCanvas(Canvas canvas) {
		canvasPanel.add(canvas, BorderLayout.CENTER);
		pack();
	}
	
	public void exit() {
		if (HexScapeCore.getInstance().isOnline()) {
			DisconnectFromServerMessage message = new DisconnectFromServerMessage();
    		CoreMessageBus.post(message);
		}
		HexScapeCore.getInstance().getHexScapeJme3Application().stop(false);
		
		Runnable exit = new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				HexScapeFrame.getInstance().dispose();
			}
		};
		
		new Thread(exit).start();
	}
}
