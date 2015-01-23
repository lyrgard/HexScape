package fr.lyrgard.hexScape.gui.desktop.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JButton;

public class GradientButton extends JButton{
	
	private static final long serialVersionUID = 8298180256466975895L;
	
	private Color color1;
	private Color color2;
	
	
	public GradientButton(String text, Color color1, Color color2) {
		super(text);
		this.color1 = color1;
		this.color2 = color2;
		setContentAreaFilled(false);
		setFocusPainted(false); // used for demonstration
	}

	@Override
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setPaint(new GradientPaint(
				new Point(0, 0), 
				color1, 
				new Point(0, getHeight()), 
				color2));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.dispose();

		super.paintComponent(g);
	}


}
