package fr.lyrgard.hexScape.gui.desktop.components.game;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseMapAction;
import fr.lyrgard.hexScape.gui.desktop.action.CreateNewGameAction;
import fr.lyrgard.hexScape.message.MapLoadedMessage;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.map.Map;

import net.miginfocom.swing.MigLayout;

public class NewGameDialog extends JDialog {

	private static final long serialVersionUID = 1619661196299179236L;
	
	private JTextField gameName;
	
	private JTextField mapNameLabel;
	
	private JButton startGame;
	
	private JSpinner playerNumber;
	
	private Game game;
	
	
	
	public NewGameDialog(boolean multiplayer) {
		setTitle("Create a new game");
		
		game = new Game();
		
		this.setLayout(new MigLayout(
				 "", // Layout Constraints
				 "[right][left]", // Column constraints
				 "[][][][]20[]" // Row constraints
				));
		Dimension dim = new Dimension(600, 400);
		setPreferredSize(dim);
		
		JLabel gameNameLabel = new JLabel("Game name :");
		gameName = new JTextField(50);
		gameName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				NewGameDialog.this.validateForm();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				NewGameDialog.this.validateForm();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				NewGameDialog.this.validateForm();
			}
		});
		
		JLabel playerNumberLabel = new JLabel("Number of players :");
		SpinnerNumberModel numberModel = null;
		if (multiplayer) {
			numberModel = new SpinnerNumberModel(2, 1, 2, 1);
		} else {
			numberModel = new SpinnerNumberModel(1, 1, 1, 1);
		}
		playerNumber = new JSpinner(numberModel);
		if (!multiplayer) {
			playerNumber.setEnabled(false);
		}
		
		JButton chooseMap = new JButton(new ChooseMapAction(null));
		mapNameLabel = new JTextField(50);
		mapNameLabel.setEditable(false);
		
		startGame = new JButton(new CreateNewGameAction(game));
		startGame.setEnabled(false);
		
		
		add(gameNameLabel);
		add(gameName, "wrap");
		
		add(playerNumberLabel);
		add(playerNumber, "wrap");
		
		add(chooseMap);
		add(mapNameLabel, "wrap");
		
		add(startGame, "span 2, align center");
		
		pack();
		
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onMapLoaded(final MapLoadedMessage message) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Map map = message.getMap();
				mapNameLabel.setText(map.getName());
				NewGameDialog.this.validateForm();
			}
		});
	}
	
	private void validateForm() {

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				if (StringUtils.isNotEmpty(mapNameLabel.getText()) && StringUtils.isNotEmpty(gameName.getText())) {
					game.setName(gameName.getText());
					game.setMap(HexScapeCore.getInstance().getHexScapeJme3Application().getScene().getMapManager().getMap());
					game.setPlayerNumber(((SpinnerNumberModel)playerNumber.getModel()).getNumber().intValue());
					NewGameDialog.this.startGame.setEnabled(true);
				} else {
					NewGameDialog.this.startGame.setEnabled(false);
				}
			}
		});
	}
}
