package fr.lyrgard.hexScape.server.service;

import org.apache.commons.lang.math.RandomUtils;

public class IdService {

	public String getNewPlayerId() {
		return Integer.toString(RandomUtils.nextInt());
	}
}
