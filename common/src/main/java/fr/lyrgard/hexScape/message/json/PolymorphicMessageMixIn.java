package fr.lyrgard.hexScape.message.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.HeartBeatMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.LeaveRoomMessage;
import fr.lyrgard.hexScape.message.MessagePostedMessage;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.message.PlayerJoinedRoomMessage;
import fr.lyrgard.hexScape.message.PostMessageMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.RoomLeftMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
@JsonSubTypes({
		@Type(value = ArmyLoadedMessage.class, name = "ArmyLoadedMessage"),
		@Type(value = ConnectedToServerMessage.class, name = "ConnectedToServerMessage"),
		@Type(value = CreateGameMessage.class, name = "CreateGameMessage"),
		@Type(value = DiceThrownMessage.class, name = "DiceThrownMessage"),
		@Type(value = DisconnectedFromServerMessage.class, name = "DisconnectedFromServerMessage"),
		@Type(value = GameCreatedMessage.class, name = "GameCreatedMessage"),
		@Type(value = GameEndedMessage.class, name = "GameEndedMessage"),
		@Type(value = GameJoinedMessage.class, name = "GameJoinedMessage"),
		@Type(value = GameStartedMessage.class, name = "GameStartedMessage"),
		@Type(value = HeartBeatMessage.class, name = "HeartBeatMessage"),
		@Type(value = JoinGameMessage.class, name = "JoinGameMessage"),
		@Type(value = JoinRoomMessage.class, name = "JoinRoomMessage"),
		@Type(value = LeaveRoomMessage.class, name = "LeaveRoomMessage"),
	    @Type(value = MessagePostedMessage.class, name = "MessagePostedMessage"),
	    @Type(value = PieceMovedMessage.class, name = "PieceMovedMessage"),
	    @Type(value = PiecePlacedMessage.class, name = "PiecePlacedMessage"),
	    @Type(value = PieceRemovedMessage.class, name = "PieceRemovedMessage"),
	    @Type(value = PieceSelectedMessage.class, name = "PieceSelectedMessage"),
	    @Type(value = PieceUnselectedMessage.class, name = "PieceUnselectedMessage"),
	    @Type(value = PostMessageMessage.class, name = "PostMessageMessage"),
	    @Type(value = RoomJoinedMessage.class, name = "RoomJoinedMessage"),
	    @Type(value = RoomLeftMessage.class, name = "RoomLeftMessage"),
	    @Type(value = StartGameMessage.class, name = "StartGameMessage"),
	    @Type(value = ThrowDiceMessage.class, name = "ThrowDiceMessage"),
	    @Type(value = UserIdAllocatedMessage.class, name = "UserIdAllocatedMessage"),
	    @Type(value = UserInformationMessage.class, name = "UserInformationMessage"),
	    @Type(value = PlayerJoinedRoomMessage.class, name = "PlayerJoinedRoomMessage"),
	    }) 
public interface PolymorphicMessageMixIn {

}
