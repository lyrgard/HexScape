package fr.lyrgard.hexscape.client.network;

import fr.lyrgard.hexScape.message.HeartBeatMessage;

public class HeartBeatGenerator implements Runnable {

	private static final int HEART_BEAT_TIME = 120000;
	
	private boolean shouldStop;
	
	@Override
	public void run() {
		shouldStop = false;
		try {
			Thread.sleep(HEART_BEAT_TIME);
		} catch (InterruptedException e) {
			System.out.println("Heart beat stop due to InterruptedException");
		}
		while(!shouldStop) {
			ClientNetwork.getInstance().send(new HeartBeatMessage());
			try {
				Thread.sleep(HEART_BEAT_TIME);
			} catch (InterruptedException e) {
				System.out.println("Heart beat stop due to InterruptedException");
				break;
			}
		}
		
	}
	
	public void stop() {
		shouldStop = true;
	}

}
