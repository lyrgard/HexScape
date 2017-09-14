package fr.lyrgard.hexScape.gui.desktop.controller.mapEditor;

import java.util.concurrent.Callable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;

public class MapEditorScreenController implements ScreenController {

	@Override
	public void bind(Nifty nifty, Screen screen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartScreen() {
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeCore.getInstance().getHexScapeJme3Application().displayMapEditor();
				return null;
			}
		});
		
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

}
