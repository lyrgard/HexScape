package fr.lyrgard.hexScape.gui.desktop.controller.home;

import org.apache.commons.lang.NotImplementedException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;

public class HomeScreenController implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private static final float BACKGROUND_IMAGE_PROPORTION_RATIO = 1.25f;

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
	}


	public String getWidthPx() {
		int screenWidth = HexScapeCore.getInstance().getHexScapeJme3Application().getContext().getSettings().getWidth();
		int screenHeight = HexScapeCore.getInstance().getHexScapeJme3Application().getContext().getSettings().getHeight();
		
		float screenRatio = screenWidth/(float)screenHeight;
		
		int width = 0;
		if (screenRatio > BACKGROUND_IMAGE_PROPORTION_RATIO) {
			// width goes to 100%, heigth adapts
			width = screenWidth;
		} else {
			// height goes to 100%, width adapts
			width = (int)(screenHeight * BACKGROUND_IMAGE_PROPORTION_RATIO);
		}

		return width+"px";
	}

	public String getHeightPx() {
		int screenWidth = HexScapeCore.getInstance().getHexScapeJme3Application().getContext().getSettings().getWidth();
		int screenHeight = HexScapeCore.getInstance().getHexScapeJme3Application().getContext().getSettings().getHeight();
		float screenRatio = screenWidth/(float)screenHeight;
		
		int height = 0;
		if (screenRatio > BACKGROUND_IMAGE_PROPORTION_RATIO) {
			// width goes to 100%, heigth adapts
			height = (int)(screenWidth / BACKGROUND_IMAGE_PROPORTION_RATIO);
		} else {
			// height goes to 100%, width adapts
			height = screenHeight;
		}

		return height+"px";


	}

}
