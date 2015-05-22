package fr.lyrgard.hexScape.gui.desktop;

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
import fr.lyrgard.hexScape.gui.desktop.controller.home.HomeScreenController;


public class HexScapeDesktopGui implements ScreenController, InitCallBack {

	private Nifty nifty;
	
	public static void main(String[] args){
		new HexScapeDesktopGui().start();
	}
	
	
	
	private void start() {
		HexScapeFrame frame = HexScapeFrame.getInstance();
		
		final HexScapeCore core = HexScapeCore.getInstance();
		final HexScapeJme3Application app = core.getHexScapeJme3Application();
		AppSettings settings = new AppSettings(true);
		settings.setWidth(frame.getCanvasContainer().getWidth());
		settings.setHeight(frame.getCanvasContainer().getHeight());
		app.setSettings(settings);
		app.createCanvas(); // create canvas!
		JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
		ctx.setSystemListener(app);
		
		app.setInitCallBack(this);
		
		frame.setJmeCanvas(ctx.getCanvas());
		
		app.startCanvas();
	}


	@Override
	public void init() {
		SimpleApplication app = HexScapeCore.getInstance().getHexScapeJme3Application();
		
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

		nifty.registerMouseCursor("buttonHoverMousePointer", "gui/images/cursorHover.png", 0, 0);
		
		// attach the nifty display to the gui view port as a processor
		app.getGuiViewPort().addProcessor(niftyDisplay);
		
		//nifty.resolutionChanged();
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
