package fr.lyrgard.hexScape.gui.desktop;

import java.awt.Dimension;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.HexScapeJme3Application;
import fr.lyrgard.hexScape.InitCallBack;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.view.common.View3d;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;


public class HexScapeDesktopGui implements ScreenController, InitCallBack {

	private Nifty nifty;
	
	public static void main(String[] args){
		HexScapeCore.getInstance();
		new HexScapeDesktopGui().start();
	}
	
	
	
	private void start() {
		HexScapeJme3Application app = HexScapeCore.getInstance().getHexScapeJme3Application();
		app.setInitCallBack(this);
		app.start();
	}


	@Override
	public void init() {
		HexScapeJme3Application app = HexScapeCore.getInstance().getHexScapeJme3Application();
		
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
				app.getInputManager(),
				app.getAudioRenderer(),
				app.getGuiViewPort());
		nifty = niftyDisplay.getNifty();
		nifty.registerScreenController(new HomeScreenController());
		nifty.addXml("gui/niftyGui.xml");
		nifty.addXml("gui/homeScreen.xml");
		nifty.addXml("gui/loadGameScreen.xml");
		nifty.addXml("gui/gameScreen.xml");
		nifty.gotoScreen("homeScreen");

		// attach the nifty display to the gui view port as a processor
		app.getGuiViewPort().addProcessor(niftyDisplay);
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}






}
