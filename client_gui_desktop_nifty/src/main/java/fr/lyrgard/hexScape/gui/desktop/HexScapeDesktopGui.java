package fr.lyrgard.hexScape.gui.desktop;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import de.lessvoid.nifty.Nifty;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.HexScapeJme3Application;
import fr.lyrgard.hexScape.InitCallBack;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.controller.home.HomeScreenController;
import fr.lyrgard.hexScape.message.WorldGetFocusMessage;


public class HexScapeDesktopGui implements InitCallBack {

	private Nifty nifty;
	
	private static final HexScapeDesktopGui INSTANCE = new HexScapeDesktopGui();
	
	private HexScapeDesktopGui() {
	}
	
	public static HexScapeDesktopGui getInstance() {
		return INSTANCE;
	}
	
	public static void main(String[] args){
		
		
		INSTANCE.start();
	}
	
	
	
	private void start() {
		
		SwingUtilities.invokeLater(new Runnable(){
            public void run(){
            	HexScapeFrame frame = HexScapeFrame.getInstance();
        		Image icone = Toolkit.getDefaultToolkit().getImage("hexscape.png");
        		frame.setIconImage(icone);
        		
        		final HexScapeCore core = HexScapeCore.getInstance();
        		final HexScapeJme3Application app = core.getHexScapeJme3Application();
        		AppSettings settings = new AppSettings(true);
        		settings.setWidth(frame.getCanvasContainer().getWidth());
        		settings.setHeight(frame.getCanvasContainer().getHeight());
        		app.setSettings(settings);
        		app.createCanvas(); // create canvas!
        		JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
        		ctx.setSystemListener(app);
        		
        		app.setInitCallBack(HexScapeDesktopGui.this);
        		
        		frame.setJmeCanvas(ctx.getCanvas());
        		
        		app.startCanvas();
            }
        });
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
		nifty.addXml("gui/onlineScreen.xml");
		nifty.addXml("gui/optionScreen.xml");
		nifty.gotoScreen("homeScreen");

		nifty.registerMouseCursor("buttonHoverMousePointer", "gui/images/cursorHover.png", 0, 0);
		
		// attach the nifty display to the gui view port as a processor
		app.getGuiViewPort().addProcessor(niftyDisplay);
		
		boolean displayStats = false;
		
		if (Boolean.TRUE.toString().equals(System.getProperty("displayStats"))) {
			displayStats = true;
		}
		app.setDisplayStatView(displayStats);
		app.setDisplayFps(displayStats);
		
		//nifty.resolutionChanged();
		
		GuiMessageBus.register(this);
	}

	@Subscribe public void onWorldGetFocus(WorldGetFocusMessage message) {
		nifty.getCurrentScreen().getFocusHandler().resetFocusElements();
	}

	public Nifty getNifty() {
		return nifty;
	}

	
}
