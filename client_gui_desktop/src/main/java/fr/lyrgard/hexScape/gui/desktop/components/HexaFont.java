package fr.lyrgard.hexScape.gui.desktop.components;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.UIManager;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;

public class HexaFont {

	private static Font font;
	
	public static Font getFont() {
		if (font == null) {
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, HexaFont.class.getResourceAsStream("/gui/font/Hexa.ttf"));
			} catch (FontFormatException | IOException e) {
				GuiMessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayerId(), "Error while loading font Hexamatter"));
				font = UIManager.getDefaults().getFont("TextField.font");
			}
		}
		return font;
	}
}
