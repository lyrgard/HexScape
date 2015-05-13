package fr.lyrgard.hexScape.gui.desktop.view.common;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.UIManager;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class HexaFont {

	private static Font font;
	
	public static Font getFont() {
		if (font == null) {
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, HexaFont.class.getResourceAsStream("/gui/font/Hexa.ttf"));
			} catch (FontFormatException | IOException e) {
				GuiMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getId(), "Error while loading font Hexamatter"));
				font = UIManager.getDefaults().getFont("TextField.font");
			}
		}
		return font;
	}
}
