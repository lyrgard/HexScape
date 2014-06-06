package fr.lyrgard.hexscape.server.network.id;

import java.util.UUID;

public class IdGenerator {

	private static final IdGenerator INSTANCE = new IdGenerator();
	
	public static IdGenerator getInstance() {
		return INSTANCE;
	}
	
	private IdGenerator() {
	}
	
	public String getNewUserId() {
		UUID newId = UUID.randomUUID();
		return newId.toString();
	}
}
