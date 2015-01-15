package fr.lyrgard.hexscape.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.message.HeartBeatMessage;

public class HeartBeatGenerator implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatGenerator.class);

	private static final int HEART_BEAT_TIME = 120000;
	
	private boolean shouldStop;
	
	@Override
	public void run() {
		shouldStop = false;
		try {
			Thread.sleep(HEART_BEAT_TIME);
		} catch (InterruptedException e) {
			LOGGER.error("Heart beat stop due to InterruptedException", e);
		}
		while(!shouldStop) {
			ClientNetwork.getInstance().send(new HeartBeatMessage());
			try {
				Thread.sleep(HEART_BEAT_TIME);
			} catch (InterruptedException e) {
				LOGGER.error("Heart beat stop due to InterruptedException", e);
				break;
			}
		}
		
	}
	
	public void stop() {
		shouldStop = true;
	}

}
