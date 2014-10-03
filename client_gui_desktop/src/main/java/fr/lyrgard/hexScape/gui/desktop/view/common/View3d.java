package fr.lyrgard.hexScape.gui.desktop.view.common;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.gui.desktop.jme3Swing.SwingContext;

public class View3d extends JPanel {

	private static final long serialVersionUID = 6296956117784219289L;

	private Canvas panel3d;
	
	public View3d(Canvas panel3d) {
		this.panel3d = panel3d;
		setLayout(new BorderLayout());
		add(panel3d, BorderLayout.CENTER);
		setFocusable(true);
		requestFocusInWindow();
		
//		final SwingContext ctx = (SwingContext)HexScapeCore.getInstance().getHexScapeJme3Application().getContext();
//		ctx.setInputSource(this);
//		
//		addMouseListener(new MouseAdapter() {
//
//			public void mousePressed(MouseEvent e) {
//				requestFocusInWindow();
//			}
//		});
//		
//		panel3d.transferFocusBackward();
	}
	
	public Component getComponent() {
		return panel3d;
	}
}
