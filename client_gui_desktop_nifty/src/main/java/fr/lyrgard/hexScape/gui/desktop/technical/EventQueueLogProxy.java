package fr.lyrgard.hexScape.gui.desktop.technical;

import java.awt.AWTEvent;
import java.awt.EventQueue;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventQueueLogProxy extends EventQueue {
	Logger logger = LoggerFactory.getLogger("Swing");

	protected void dispatchEvent(AWTEvent newEvent) {
		
		try {
			super.dispatchEvent(newEvent);
		} catch (Throwable t) {
			logger.error("Unexpected error caught in Swing Event Queue", t);
			
			String message = t.getMessage();
			if (message == null) {
				message = "Unexpected error in " + t.getClass();
			}

			JOptionPane.showMessageDialog(null, "General Error", t.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
