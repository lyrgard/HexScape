package fr.lyrgard.hexScape.gui.desktop.components.config;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.message.WarningMessage;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class ConfigPanel extends JPanel {

	private static final long serialVersionUID = 5960578803189702453L;

	private JTextField username;
	
	private JTextField serverHost;
	
	private JButton saveButton;
	
	
	
	public ConfigPanel() {
		this.setLayout(new MigLayout(
				 "wrap", // Layout Constraints
				 "[right][left]", // Column constraints
				 "[][][]20[]" // Row constraints
				));
		
		JLabel usernameLabel = new JLabel("User name :");
		username = new JTextField(50);
		username.setText(ConfigurationService.getInstance().getUserName());
		
		JLabel serverHostLabel = new JLabel("Server url :");
		serverHost = new JTextField(50);
		serverHost.setText(ConfigurationService.getInstance().getServerHost());
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!username.getText().trim().isEmpty()) {
					ConfigurationService.getInstance().setUserName(username.getText());
					ConfigurationService.getInstance().setServerHost(serverHost.getText());
					ConfigurationService.getInstance().save();
					GuiMessageBus.post(new InfoMessage(HexScapeCore.getInstance().getPlayerId(), "Configuration saved"));
				} else {
					GuiMessageBus.post(new WarningMessage(HexScapeCore.getInstance().getPlayerId(), "user name cannot be empty"));
				}
				
			}
		});
		
		
		add(usernameLabel);
		add(username);
		
		add(serverHostLabel);
		add(serverHost);
		
		add(saveButton, "span 2, align center");

	}
}
