package fr.lyrgard.hexScape.gui.desktop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


import com.google.common.eventbus.Subscribe;


import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ArmyLoadedEvent;
import fr.lyrgard.hexScape.event.ErrorEvent;
import fr.lyrgard.hexScape.event.JME3ReadyEvent;
import fr.lyrgard.hexScape.event.MessageEvent;
import fr.lyrgard.hexScape.event.WarningEvent;
import fr.lyrgard.hexScape.gui.desktop.card.ArmyCardPanel;
import fr.lyrgard.hexScape.gui.desktop.card.SelectedCardPanel;
import fr.lyrgard.hexScape.gui.desktop.card.SelectedPiecePanel;
import fr.lyrgard.hexScape.gui.desktop.menu.MenuBar;
import fr.lyrgard.hexScape.model.Card;
import fr.lyrgard.hexScape.model.CardCollection;

public class HexScapeFrame extends JFrame {

	private static final long serialVersionUID = 7043232675085791117L;

	public static HexScapeFrame instance; 

	public static HexScapeFrame getInstance() {
		return instance;
	}

	private Canvas panel3d;
	
	
	private JPanel panel;

	private JPanel armyPanel;

	private SelectedPiecePanel selectedPiecePanel;

	public HexScapeFrame(final Canvas panel3d) {
		super("HexScape");

		
		instance = this;
		this.panel3d = panel3d;
		
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

		// Fill Swing window with canvas and swing components
		panel = new JPanel(new BorderLayout()); // a panel
		final JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		panel.add(leftPanel, BorderLayout.LINE_START);

		setJMenuBar(new MenuBar());




		armyPanel = new JPanel();
		armyPanel.setLayout(new BoxLayout(armyPanel, BoxLayout.Y_AXIS));
		final JScrollPane scrollPane = new JScrollPane(armyPanel);
		scrollPane.setPreferredSize(new Dimension(180, 500));
		scrollPane.setMaximumSize(new Dimension(180, 500));
		leftPanel.add(scrollPane);
		leftPanel.add(new SelectedCardPanel());
		leftPanel.add(new SelectedPiecePanel());
		
		
		final JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(panel3d, BorderLayout.CENTER);
		centerPanel.setFocusable(true);
		centerPanel.requestFocusInWindow();
		
		final SwingContext ctx = (SwingContext)HexScapeCore.getInstance().getHexScapeJme3Application().getContext();
		ctx.setInputSource(centerPanel);
		
		panel.add(centerPanel, BorderLayout.CENTER);
		

		panel.add(new LeftPanel(), BorderLayout.LINE_END);
		
		add(panel);

		HexScapeCore.getInstance().getEventBus().register(this);     
		if (HexScapeCore.getInstance().getHexScapeJme3Application().isReady()) {
			onJme3Ready(null);
		}
		
		centerPanel.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				centerPanel.requestFocusInWindow();
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		

		// Display Swing window including JME canvas!
		setExtendedState(JFrame.MAXIMIZED_BOTH); // aligns itself with windows task bar
		// set maximum screen   
		setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		setVisible(true);
		
		panel3d.transferFocusBackward();
	}

	@Subscribe public void onArmyLoaded(final ArmyLoadedEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				CardCollection army = event.getArmy();
				if (armyPanel != null) {
					armyPanel.removeAll();
					for (String cardId : army.getCardsById().keySet()) {
						Card card = army.getCardsById().get(cardId);
						Integer number = army.getNumberById().get(cardId);
						armyPanel.add(new ArmyCardPanel(card, number));

					}
					armyPanel.validate();
					armyPanel.repaint();
				}
				HexScapeFrame.this.pack();
			}
		});
	}

	@Subscribe public void onMessage(final MessageEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {

				String message = event.getMessage();

				int messageType = JOptionPane.INFORMATION_MESSAGE;
				String title = "Message";
				if (event instanceof ErrorEvent) {
					messageType = JOptionPane.ERROR_MESSAGE;
					title = "Error";
				} else if (event instanceof WarningEvent) {
					messageType = JOptionPane.WARNING_MESSAGE;
					title = "Warning";
				}
				JOptionPane.showMessageDialog(null, message, title, messageType);
			}
		});

	}
	
	@Subscribe public void onJme3Ready(final JME3ReadyEvent event) {
		//HexScapeCore.getInstance().getHexScapeJme3Application().getInputManager().addRawInputListener(new InputListener(this));
		//((AwtPanel)panel3d).attachTo(false, HexScapeCore.getInstance().getHexScapeJme3Application().getViewPort());
		//((AwtPanel)panel3d).attachTo(false, HexScapeCore.getInstance().getHexScapeJme3Application().getGuiViewPort());
	}
}
