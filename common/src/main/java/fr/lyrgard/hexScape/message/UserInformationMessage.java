package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.player.ColorEnum;

public class UserInformationMessage extends AbstractMessage {

	private String name;
	
	private ColorEnum color;

	@JsonCreator
	public UserInformationMessage(
			@JsonProperty("name") String name, 
			@JsonProperty("color") ColorEnum color) {
		super();
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public ColorEnum getColor() {
		return color;
	}
	
	
	
}
