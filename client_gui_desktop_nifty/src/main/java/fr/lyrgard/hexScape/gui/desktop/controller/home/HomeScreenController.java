package fr.lyrgard.hexScape.gui.desktop.controller.home;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;

public class HomeScreenController implements ScreenController {

	private Nifty nifty;
	private Screen screen;

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		HexScapeCore.getInstance().getHexScapeJme3Application().displayTitleScreen();
		//screen.getRootElement().startEffect(EffectEventId.onCustom, null,"startScreen");
	}
}
