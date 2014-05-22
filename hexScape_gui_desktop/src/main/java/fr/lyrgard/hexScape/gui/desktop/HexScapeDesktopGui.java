package fr.lyrgard.hexScape.gui.desktop;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.jme3.system.AppSettings;

import fr.lyrgard.hexScape.HexScapeCore;


public class HexScapeDesktopGui {
	
	public static void main(String[] args){
		final HexScapeCore app = HexScapeCore.getInstance();
		
		AppSettings settings = new AppSettings(true);
		//settings.setCustomRenderer(AwtPanelsContext.class);
		settings.setCustomRenderer(SwingContext.class);
		settings.setFrameRate(60);
		settings.setWidth(1024);
		settings.setWidth(768);
		
		app.getHexScapeJme3Application().setShowSettings(false);
		app.getHexScapeJme3Application().setSettings(settings);
		
		//app.getHexScapeJme3Application().start();
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				AwtPanelsContext ctx = (AwtPanelsContext) app.getHexScapeJme3Application().getContext();
//				AwtPanel panel3d = ctx.createPanel(PaintMode.Accelerated);
                
				

				
				
				
				
				app.getHexScapeJme3Application().createCanvas();
				
				final SwingContext ctx = (SwingContext)app.getHexScapeJme3Application().getContext();
				
				ctx.setSystemListener(app.getHexScapeJme3Application());
				
				final Canvas panel3d = ctx.getCanvas();
				//panel3d.setFocusable(true);
				
				Dimension dim = new Dimension(1024, 768);
				panel3d.setMinimumSize(dim);
				panel3d.setEnabled(false);
				
				app.getHexScapeJme3Application().startCanvas();
				
		        new HexScapeFrame(panel3d);
		        
		        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			}
		});


	}
	
	
}
