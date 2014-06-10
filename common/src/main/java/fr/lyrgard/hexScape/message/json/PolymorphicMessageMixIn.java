package fr.lyrgard.hexScape.message.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.PostMessageMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
@JsonSubTypes({
		@Type(value = ConnectedToServerMessage.class, name = "ConnectedToServerMessage"),
		@Type(value = ArmyLoadedMessage.class, name = "ArmyLoadedMessage"),
		@Type(value = JoinRoomMessage.class, name = "JoinRoomMessage"),
	    @Type(value = MessagePostedMessage.class, name = "MessagePostedMessage"),
	    @Type(value = PostMessageMessage.class, name = "PostMessageMessage"),
	    @Type(value = RoomJoinedMessage.class, name = "RoomJoinedMessage"),
	    @Type(value = UserIdAllocatedMessage.class, name = "UserIdAllocatedMessage"),
	    @Type(value = UserInformationMessage.class, name = "UserInformationMessage"),
	    @Type(value = PlayerJoinedRoomMessage.class, name = "PlayerJoinedRoomMessage"),
	    
	    }) 
public interface PolymorphicMessageMixIn {

}
