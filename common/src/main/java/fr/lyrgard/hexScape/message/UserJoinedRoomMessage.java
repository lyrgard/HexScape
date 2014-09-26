package fr.lyrgard.hexScape.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lyrgard.hexScape.model.player.ColorEnum;

public class UserJoinedRoomMessage extends AbstractUserMessage {
	
	private String name;
	
	private ColorEnum color;
	
	private String roomId;

	@JsonCreator
	public UserJoinedRoomMessage(
			@JsonProperty("userId") String userId, 
			@JsonProperty("name") String name, 
			@JsonProperty("color") ColorEnum color, 
			@JsonProperty("roomId") String roomId) {
		super(userId);
		this.roomId = roomId;
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public ColorEnum getColor() {
		return color;
	}

	public String getRoomId() {
		return roomId;
	}
	
	

}
