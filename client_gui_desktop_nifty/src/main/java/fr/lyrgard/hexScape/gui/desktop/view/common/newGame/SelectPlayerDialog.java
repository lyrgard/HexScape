package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;

public class SelectPlayerDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3575478753881892092L;
	private String selectedPlaceholderId;
	
	public SelectPlayerDialog(Game game, final PlayerSelectedListener listener) {
		Collection<Player> freePlayers = game.getFreePlayers();
		if (!freePlayers.isEmpty()) {
			selectedPlaceholderId = freePlayers.iterator().next().getId();
		}
		
		setLocationRelativeTo(HexScapeFrame.getInstance());
		setTitle("Choose your role");
		
		this.setLayout(new MigLayout(
				 "", // Layout Constraints
				 "[right][left]", // Column constraints
				 "[100]20[]" // Row constraints
				));
		Dimension dim = new Dimension(600, 400);
		setPreferredSize(dim);
		
		final JButton chooseRoleButton = new JButton("Choose this role");
		chooseRoleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.playerSelected(selectedPlaceholderId);
				SelectPlayerDialog.this.dispose();
			}
		});
		chooseRoleButton.setEnabled(false);
		
		JLabel availableRolesLabel = new JLabel("Available roles :");
		PlayerListModel placeholderListModel = new PlayerListModel(freePlayers);
		final PlaceholderList placeholderList = new PlaceholderList(placeholderListModel);
		placeholderList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Player placeholder = placeholderList.getSelectedValue();
					selectedPlaceholderId = placeholder.getId();
					chooseRoleButton.setEnabled(true);
				}
				
			}
		});
		
		add(availableRolesLabel);
		add(placeholderList, "grow, wrap");
		add(chooseRoleButton, "span 2, align center");
		
		pack();
		
	}
}
