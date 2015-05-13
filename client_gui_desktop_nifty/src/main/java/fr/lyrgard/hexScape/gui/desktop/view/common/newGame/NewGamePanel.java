package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseMapAction;
import fr.lyrgard.hexScape.gui.desktop.action.CreateNewGameAction;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;
import net.miginfocom.swing.MigLayout;

public class NewGamePanel extends JPanel {

	private static final long serialVersionUID = 1619661196299179236L;
	
	private JTextField gameName;
	
	private JTextField mapNameLabel;
	
	private JButton newGame;
	
	private JSpinner playerNumber;
	
	private Map map;
	
	private Game game;
	
	
	
	public NewGamePanel(Component parent) {
		game = new Game();
		
		this.setLayout(new MigLayout(
				 "", // Layout Constraints
				 "[right][left]", // Column constraints
				 "[][][][]20[]" // Row constraints
				));
		
		JLabel gameNameLabel = new JLabel("Game name :");
		gameName = new JTextField(50);
		gameName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				NewGamePanel.this.validateForm();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				NewGamePanel.this.validateForm();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				NewGamePanel.this.validateForm();
			}
		});
		
		JLabel playerNumberLabel = new JLabel("Number of players :");
		SpinnerNumberModel numberModel = null;
		//if (multiplayer) {
			numberModel = new SpinnerNumberModel(1, 1, 4, 1);
//		} else {
//			numberModel = new SpinnerNumberModel(1, 1, 1, 1);
//		}
		playerNumber = new JSpinner(numberModel);
		//if (!multiplayer) {
		//	playerNumber.setEnabled(false);
		//}
		
		JButton chooseMap = new JButton(new ChooseMapAction(null));
		mapNameLabel = new JTextField(50);
		mapNameLabel.setEditable(false);
		
		newGame = new JButton(new CreateNewGameAction(game));
		
		newGame.setEnabled(false);
		
		
		add(gameNameLabel);
		add(gameName, "wrap");
		
		add(playerNumberLabel);
		add(playerNumber, "wrap");
		
		add(chooseMap);
		add(mapNameLabel, "wrap");
		
		add(newGame, "span 2, align center");
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onMapLoaded(final MapLoadedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Map map = message.getMap();
				mapNameLabel.setText(map.getName());
				NewGamePanel.this.map = map;
				NewGamePanel.this.validateForm();
			}
		});
	}
	
	private void validateForm() {

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (StringUtils.isNotEmpty(mapNameLabel.getText()) && map != null ) {
					game.setName(gameName.getText());
					game.setMap(map);
					game.setPlayerNumber(((SpinnerNumberModel)playerNumber.getModel()).getNumber().intValue());
					NewGamePanel.this.newGame.setEnabled(true);
				} else {
					NewGamePanel.this.newGame.setEnabled(false);
				}
			}
		});
	}
}
