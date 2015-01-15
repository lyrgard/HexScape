package fr.lyrgard.hexScape.gui.desktop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.gui.desktop.technical.SwingContext;
import fr.lyrgard.hexScape.gui.desktop.view.common.View3d;
import fr.lyrgard.hexScape.message.DisconnectFromServerMessage;


public class HexScapeDesktopGui {
	
	public static void main(String[] args){
		final HexScapeCore core = HexScapeCore.getInstance();
		final SimpleApplication app = core.getHexScapeJme3Application();
		
		AppSettings settings = new AppSettings(true);
		//settings.setCustomRenderer(SwingContext.class);
		settings.setFrameRate(60);
		settings.setWidth(102);
		settings.setWidth(77);
		
		
		app.setShowSettings(false);
		app.setSettings(settings);
		
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createCanvas();
				
				JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
				ctx.setSystemListener(app);
				Dimension dim = new Dimension(640, 480);
				ctx.getCanvas().setPreferredSize(dim);
				
				new HexScapeFrame(new View3d(ctx.getCanvas()));
				
				app.startCanvas();
				
//				app.getHexScapeJme3Application().createCanvas();
//				
//				final SwingContext ctx = (SwingContext)app.getHexScapeJme3Application().getContext();
//				
//				ctx.setSystemListener(app.getHexScapeJme3Application());
//				
//				final Canvas panel3d = ctx.getCanvas();
//				
//				Dimension dim = new Dimension(102, 77);
//				panel3d.setMinimumSize(dim);
//				panel3d.setEnabled(false);
//				
//				app.getHexScapeJme3Application().startCanvas();
//				
//		        new HexScapeFrame(new View3d(panel3d));
		        
		        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	        	DisconnectFromServerMessage message = new DisconnectFromServerMessage();
	        	CoreMessageBus.post(message);
	        }
	    }, "Disconnect from server"));
	}
	
	
}
