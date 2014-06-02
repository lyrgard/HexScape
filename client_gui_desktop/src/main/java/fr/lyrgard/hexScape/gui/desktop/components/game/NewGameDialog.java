package fr.lyrgard.hexScape.gui.desktop.components.game;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ArmyLoadedEvent;
import fr.lyrgard.hexScape.event.MapLoadedEvent;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseArmyAction;
import fr.lyrgard.hexScape.gui.desktop.action.ChooseMapAction;
import fr.lyrgard.hexScape.gui.desktop.navigation.ViewEnum;
import fr.lyrgard.hexScape.model.card.CardCollection;
import fr.lyrgard.hexScape.model.map.Map;

import net.miginfocom.swing.MigLayout;

public class NewGameDialog extends JDialog {

	private static final long serialVersionUID = 1619661196299179236L;
	
	JTextField mapNameLabel;
	
	JTextField armyNameLabel;
	
	public NewGameDialog(boolean multiplayer) {
		setTitle("Create a new game");
		this.setLayout(new MigLayout(
				 "", // Layout Constraints
				 "[right][left]", // Column constraints
				 "[][][][]20[]" // Row constraints
				));
		Dimension dim = new Dimension(600, 400);
		setPreferredSize(dim);
		
		JLabel gameNameLabel = new JLabel("Game name :");
		JTextField gameName = new JTextField(50);
		
		JLabel playerNumberLabel = new JLabel("Number of players :");
		SpinnerNumberModel numberModel = null;
		if (multiplayer) {
			numberModel = new SpinnerNumberModel(2, 1, 2, 1);
		} else {
			numberModel = new SpinnerNumberModel(1, 1, 1, 1);
		}
		JSpinner playerNumber = new JSpinner(numberModel);
		if (!multiplayer) {
			playerNumber.setEnabled(false);
		}
		
		JButton chooseMap = new JButton(new ChooseMapAction(null));
		mapNameLabel = new JTextField(50);
		mapNameLabel.setEditable(false);
		
		JButton chooseArmy = new JButton(new ChooseArmyAction(null));
		armyNameLabel = new JTextField(50);
		armyNameLabel.setEditable(false);
		
		ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/addGame.png"));
		JButton startGame = new JButton("Start", icon);
		startGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HexScapeFrame.getInstance().showView(ViewEnum.GAME);
				NewGameDialog.this.dispose();
			}
		});
		
		
		add(gameNameLabel);
		add(gameName, "wrap");
		
		add(playerNumberLabel);
		add(playerNumber, "wrap");
		
		add(chooseMap);
		add(mapNameLabel, "wrap");
		
		add(chooseArmy);
		add(armyNameLabel, "wrap");
		
		add(startGame, "span 2, align center");
		
		pack();
		
		HexScapeCore.getInstance().getEventBus().register(this);
	}
	
	@Subscribe public void onArmyLoaded(final ArmyLoadedEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				CardCollection army = event.getArmy();
				armyNameLabel.setText(army.getName());
			}
		});
	}
	
	@Subscribe public void onMapLoaded(final MapLoadedEvent event) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				Map map = event.getMap();
				mapNameLabel.setText(map.getName());
			}
		});
	}
}
