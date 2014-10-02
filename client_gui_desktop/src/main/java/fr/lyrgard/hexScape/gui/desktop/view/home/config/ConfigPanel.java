package fr.lyrgard.hexScape.gui.desktop.view.home.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.message.WarningMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.service.AssetService;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class ConfigPanel extends JPanel {

	private static final long serialVersionUID = 5960578803189702453L;

	private JTextField username;
	
	private JTextField serverHost;
	
	private JComboBox<String> gameList;
	
	private JButton saveButton;
	
	
	public ConfigPanel() {
		this.setLayout(new MigLayout(
				 "wrap", // Layout Constraints
				 "[right][left]", // Column constraints
				 "[][][][]20[]" // Row constraints
				));
		
		JLabel usernameLabel = new JLabel("User name :");
		username = new JTextField(50);
		username.setText(ConfigurationService.getInstance().getUserName());
		
		JLabel serverHostLabel = new JLabel("Server url :");
		serverHost = new JTextField(50);
		serverHost.setText(ConfigurationService.getInstance().getServerHost());
		
		JLabel gameListLabel = new JLabel("Game :");
		gameList = new JComboBox<>();
		gameList.removeAll();
		for (String game : ConfigurationService.getInstance().getGameFolders()) {
			gameList.addItem(game);
		}
		gameList.setSelectedItem(ConfigurationService.getInstance().getGameFolder());
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!username.getText().trim().isEmpty()) {
					boolean gameChanged = !ConfigurationService.getInstance().getGameFolder().equals((String)gameList.getSelectedItem());
					ConfigurationService.getInstance().setUserName(username.getText());
					ConfigurationService.getInstance().setServerHost(serverHost.getText());
					ConfigurationService.getInstance().setGameFolder((String)gameList.getSelectedItem());
					ConfigurationService.getInstance().save();
					if (gameChanged) {
						AssetService.getInstance().reloadAssets();
					}
					GuiMessageBus.post(new InfoMessage(CurrentUserInfo.getInstance().getId(), "Configuration saved"));
				} else {
					GuiMessageBus.post(new WarningMessage(CurrentUserInfo.getInstance().getId(), "user name cannot be empty"));
				}
				
			}
		});
		
		
		add(usernameLabel);
		add(username);
		
		add(serverHostLabel);
		add(serverHost);
		
		add(gameListLabel);
		add(gameList);
		
		add(saveButton, "span 2, align center");

	}
}
