package fr.lyrgard.hexScape.gui.desktop.view.common.newGame;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import net.miginfocom.swing.MigLayout;

public class CreateGameDialog extends JDialog {

	private static final long serialVersionUID = 8394314030790740545L;

	public CreateGameDialog(Component parent) {
		this.setLayout(new MigLayout(
				 "", // Layout Constraints
				 "[]", // Column constraints
				 "[]" // Row constraints
				));
		Dimension dim = new Dimension(600, 400);
		setPreferredSize(dim);
		
		JTabbedPane tabs = new JTabbedPane();
		add(tabs, "grow");
		
		tabs.addTab("New game", new NewGamePanel(this));
		tabs.addTab("Restore saved game", new RestoreGamePanel(this));
		
		pack();
		GuiMessageBus.register(this);
	}
	
	@Subscribe public void onGameCreated(GameCreatedMessage message) {
		if (CurrentUserInfo.getInstance().getId().equals(message.getUserId())) {
			dispose();
		}
	}
}
