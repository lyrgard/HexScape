package fr.lyrgard.hexScape.listener;

import com.google.common.eventbus.Subscribe;
import com.jme3.system.AppSettings;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ChangeSamplingMessage;
import fr.lyrgard.hexScape.message.ChangeShadowQualityMessage;

public class DisplayListener {

	private static DisplayListener instance;
	
	public static void start() {
		if (instance == null) {
			instance = new DisplayListener();
			CoreMessageBus.register(instance);
		}
	}
	
	private DisplayListener() {
	}
	
	@Subscribe public void onChangeSampling(ChangeSamplingMessage message) {
		AppSettings settings = new AppSettings(false);
		int value = 0;
		switch(message.getQuality()) {
		case DESACTIVATE:
			value = 0;
			break;
		case LOW_QUALITY:
			value = 1;
			break;
		case MEDIUM_QUALITY:
			value = 2;
			break;
		case BEST_QUALITY:
			value = 4;
			break;
		}
		settings.setSamples(value);
		HexScapeCore.getInstance().getHexScapeJme3Application().setSettings(settings);
		HexScapeCore.getInstance().getHexScapeJme3Application().restart();
	}
	
	@Subscribe public void onChangeShadowQuality(ChangeShadowQualityMessage message) {
		int value = 0;
		switch(message.getQuality()) {
		case DESACTIVATE:
			value = 0;
			break;
		case LOW_QUALITY:
			value = 1024;
			break;
		case MEDIUM_QUALITY:
			value = 2048;
			break;
		case BEST_QUALITY:
			value = 4096;
			break;
		}
		//HexScapeCore.getInstance().getHexScapeJme3Application().changeShadowQuality(value);
	}
}
