package fr.lyrgard.hexScape.model;

import fr.lyrgard.hexScape.model.player.User;

public class CurrentUserInfo {
	
	private static User currentUser = new User();
	
	public static User getInstance() {
		return currentUser;
	}
	
	private CurrentUserInfo() {
	}
	
}
