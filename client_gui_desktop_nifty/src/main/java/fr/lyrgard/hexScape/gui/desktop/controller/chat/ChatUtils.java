package fr.lyrgard.hexScape.gui.desktop.controller.chat;

import fr.lyrgard.hexScape.model.player.Player;

public class ChatUtils {

	public static String getHeaderStyle(Player player) {
    	String style = "userText";
    	if (player != null) {
    		switch (player.getColor()) {
    		case RED:
    			style += "Red";
    			break;
    		case GREEN:
    			style += "Green";
    			break;
    		case BLUE:
    			style += "Blue";
    			break;
    		default:
    		}
    	}
    	return style;
    }
}
