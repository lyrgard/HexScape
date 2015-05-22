package fr.lyrgard.hexScape.gui.desktop.controller.game;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.game.Game;

public class GameScreenController implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private Game game;
	private ArmiesPanelController armiesPanelController;
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onStartScreen() {
		game = CurrentUserInfo.getInstance().getGame();
		
		
		ScrollPanel armiesScrollPanel = screen.findNiftyControl("armiesScrollPanel", ScrollPanel.class);
		armiesPanelController = new ArmiesPanelController(nifty, screen, armiesScrollPanel);
		armiesPanelController.setPlayers(game.getPlayers());
		
		GuiMessageBus.register(armiesPanelController);
	}
	
	@Override
	public void onEndScreen() {
		GuiMessageBus.unregister(armiesPanelController);
	}

	

}
