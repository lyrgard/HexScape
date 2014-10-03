package fr.lyrgard.hexScape.model;

import java.util.UUID;

public class IdGenerator {

	private static final IdGenerator INSTANCE = new IdGenerator();
	
	public static IdGenerator getInstance() {
		return INSTANCE;
	}
	
	private IdGenerator() {
	}
	
	public String getNewPlayerId() {
		UUID newId = UUID.randomUUID();
		return newId.toString();
	}
	
	public String getNewUserId() {
		UUID newId = UUID.randomUUID();
		return newId.toString();
	}
	
	public String getNewGameId() {
		UUID newId = UUID.randomUUID();
		return newId.toString();
	}
	
	public String getNewMarkerId() {
		UUID newId = UUID.randomUUID();
		return newId.toString();
	}
	
	public String getNewPieceId() {
		UUID newId = UUID.randomUUID();
		return newId.toString();
	}
}
