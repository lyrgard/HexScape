package fr.lyrgard.hexScape.gui.desktop.view.home.config;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.WarningMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class IntegerFilter extends DocumentFilter {

	private boolean test(String text) {
		if (text.trim().isEmpty()) {
			return true;
		}
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string,
			AttributeSet attr) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.insert(offset, string);

		if (test(sb.toString())) {
			super.insertString(fb, offset, string, attr);
		} else {
			GuiMessageBus.post(new WarningMessage(CurrentUserInfo.getInstance().getId(), "Only numbers can be entered in this field"));
		}
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length)	throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.delete(offset, offset + length);

		if (test(sb.toString())) {
			super.remove(fb, offset, length);
		} else {

		}
	}



	@Override
	public void replace(FilterBypass fb, int offset, int length, String text,
			AttributeSet attrs) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.replace(offset, offset + length, text);

		if (test(sb.toString())) {
			super.replace(fb, offset, length, text, attrs);
		} else {
			GuiMessageBus.post(new WarningMessage(CurrentUserInfo.getInstance().getId(), "Only numbers can be entered in this field"));
		}
	}

}
