package fr.lyrgard.hexScape.gui.desktop.controller.home;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.CannotConnectToServerMessage;
import fr.lyrgard.hexScape.message.ConnectToServerMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class HomeScreenController implements ScreenController {

	final private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	
	private Nifty nifty;
	private Screen screen;
	private Element unableToConnectToServerMessage;
	private Element connectingToServerPopup;

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
		
		unableToConnectToServerMessage = screen.findElementByName("unableToConnectToServerMessage");
		
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		GuiMessageBus.unregister(this);
	}

	@Override
	public void onStartScreen() {
		HexScapeCore.getInstance().getHexScapeJme3Application().displayTitleScreen();
		//screen.getRootElement().startEffect(EffectEventId.onCustom, null,"startScreen");
		
		GuiMessageBus.register(this);
	}
	
	public void connectToServer() {
		connectingToServerPopup = nifty.createPopup("connectingToServerPopup");
		nifty.showPopup(nifty.getCurrentScreen(), connectingToServerPopup.getId(), null);
		
		String userId = CurrentUserInfo.getInstance().getId();
		
		ConnectToServerMessage message = new ConnectToServerMessage(userId, ConfigurationService.getInstance().getServerHost());
		CoreMessageBus.post(message);
	}
	
	@Subscribe
	public void onRoomJoined(RoomJoinedMessage message) {
		if (connectingToServerPopup != null) {
			nifty.closePopup(connectingToServerPopup.getId());
			connectingToServerPopup = null;
		}
		
		nifty.gotoScreen("onlineScreen");
	}
	
	@Subscribe
	public void onCannotConnectToServer(CannotConnectToServerMessage message) {
		if (connectingToServerPopup != null) {
			nifty.closePopup(connectingToServerPopup.getId());
			connectingToServerPopup = null;
		}
		
		unableToConnectToServerMessage.setVisible(true);
		
		exec.schedule(new Runnable() {
			
			@Override
			public void run() {
				unableToConnectToServerMessage.setVisible(false);
			}
		}, 2, TimeUnit.SECONDS);
	}
}
