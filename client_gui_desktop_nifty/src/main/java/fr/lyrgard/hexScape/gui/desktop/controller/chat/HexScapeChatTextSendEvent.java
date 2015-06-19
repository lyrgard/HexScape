package fr.lyrgard.hexScape.gui.desktop.controller.chat;

import de.lessvoid.nifty.NiftyEvent;

/**
 *
 * @author ractoc
 */
public class HexScapeChatTextSendEvent implements NiftyEvent<Void> {
    private HexScapeChatControl chatControl;
    private String text;

    public HexScapeChatTextSendEvent(final HexScapeChatControl chatControl, final String textParam) {
      this.chatControl = chatControl;
      this.text = textParam;
    }

    public HexScapeChatControl getChatControl() {
      return chatControl;
    }

    public String getText() {
        return text;
    }
    
}

