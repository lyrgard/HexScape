package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.message.WarningMessage;

public class ErrorMessageListener {

	private static ErrorMessageListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new ErrorMessageListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private ErrorMessageListener() {
	}
	
	@Subscribe public void onErrorMessage(ErrorMessage message) {
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onWarningMessage(WarningMessage message) {
		GuiMessageBus.post(message);
	}
	
	@Subscribe public void onInfoMessage(InfoMessage message) {
		GuiMessageBus.post(message);
	}
}
