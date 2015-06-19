package fr.lyrgard.hexScape.gui.desktop.controller.chat;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import fr.lyrgard.hexScape.gui.desktop.HexScapeDesktopGui;
import fr.lyrgard.hexScape.model.player.User;

public class ChatUserViewConverter implements ListBoxViewConverter<User> {
	private static final String CHAT_LINE_ICON = "#chat-line-icon";
    private static final String CHAT_LINE_TEXT = "#chat-line-text";
    
    private NiftyImage playerIcon;
    private NiftyImage observerIcon;

    /**
     * Default constructor.
     */
    public ChatUserViewConverter() {
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void display(final Element listBoxItem, final User user) {
        final Element text = listBoxItem.findElementByName(CHAT_LINE_TEXT);
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        final Element icon = listBoxItem.findElementByName(CHAT_LINE_ICON);
        final ImageRenderer iconRenderer = icon.getRenderer(ImageRenderer.class);
        if (user != null) {
            textRenderer.setText(user.getName());
            if (user.isPlayingGame()) {
            	iconRenderer.setImage(getPlayerIcon());
            } else {
            	iconRenderer.setImage(getObserverIcon());
            }
            text.setStyle(ChatUtils.getHeaderStyle(user.getPlayer()));
        } else {
            textRenderer.setText("");
            iconRenderer.setImage(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getWidth(final Element listBoxItem, final User user) {
        final Element text = listBoxItem.findElementByName(CHAT_LINE_TEXT);
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        return ((textRenderer.getFont() == null) ? 0 : textRenderer.getFont().getWidth(user.getName()))
                + getObserverIcon().getWidth();
    }

	private NiftyImage getPlayerIcon() {
		if (playerIcon == null) {
			playerIcon = HexScapeDesktopGui.getInstance().getNifty().createImage("/gui/images/chat/player.png", true);
		}
		return playerIcon;
	}

	private NiftyImage getObserverIcon() {
		if (observerIcon == null) {
			observerIcon = HexScapeDesktopGui.getInstance().getNifty().createImage("/gui/images/chat/observer.png", true);
		}
		return observerIcon;
	}
}
