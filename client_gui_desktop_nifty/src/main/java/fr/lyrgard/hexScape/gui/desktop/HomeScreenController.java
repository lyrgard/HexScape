package fr.lyrgard.hexScape.gui.desktop;

import org.apache.commons.lang.NotImplementedException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

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
		// TODO Auto-generated method stub

	}
	
//	@NiftyEventSubscriber(id="soloGameButton")
//	public void soloButtonOnClick(String id, NiftyMousePrimaryClickedEvent event) {
//		nifty.gotoScreen("gameScreen");
//	}



}
