package fr.lyrgard.hexScape.message.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import fr.lyrgard.hexScape.message.ConnectedToServerMessage;
import fr.lyrgard.hexScape.message.ArmyLoadedMessage;
import fr.lyrgard.hexScape.message.CreateGameMessage;
import fr.lyrgard.hexScape.message.DiceThrownMessage;
import fr.lyrgard.hexScape.message.DisconnectedFromServerMessage;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.message.GameCreatedMessage;
import fr.lyrgard.hexScape.message.GameEndedMessage;
import fr.lyrgard.hexScape.message.GameJoinedMessage;
import fr.lyrgard.hexScape.message.GameLeftMessage;
import fr.lyrgard.hexScape.message.GameMessagePostedMessage;
import fr.lyrgard.hexScape.message.GameObservedMessage;
import fr.lyrgard.hexScape.message.GameStartedMessage;
import fr.lyrgard.hexScape.message.HeartBeatMessage;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.message.JoinGameMessage;
import fr.lyrgard.hexScape.message.JoinRoomMessage;
import fr.lyrgard.hexScape.message.LeaveGameMessage;
import fr.lyrgard.hexScape.message.LeaveRoomMessage;
import fr.lyrgard.hexScape.message.MarkerPlacedMessage;
import fr.lyrgard.hexScape.message.MarkerRemovedMessage;
import fr.lyrgard.hexScape.message.MarkerRevealedMessage;
import fr.lyrgard.hexScape.message.ObserveGameMessage;
import fr.lyrgard.hexScape.message.PieceSecondarySelectedMessage;
import fr.lyrgard.hexScape.message.PieceSecondaryUnselectedMessage;
import fr.lyrgard.hexScape.message.PostGameMessageMessage;
import fr.lyrgard.hexScape.message.RestoreGameMessage;
import fr.lyrgard.hexScape.message.RoomMessagePostedMessage;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.message.PlaceMarkerMessage;
import fr.lyrgard.hexScape.message.UserJoinedRoomMessage;
import fr.lyrgard.hexScape.message.PostRoomMessageMessage;
import fr.lyrgard.hexScape.message.RemoveMarkerMessage;
import fr.lyrgard.hexScape.message.RevealMarkerMessage;
import fr.lyrgard.hexScape.message.RoomJoinedMessage;
import fr.lyrgard.hexScape.message.RoomLeftMessage;
import fr.lyrgard.hexScape.message.StartGameMessage;
import fr.lyrgard.hexScape.message.ThrowDiceMessage;
import fr.lyrgard.hexScape.message.UserIdAllocatedMessage;
import fr.lyrgard.hexScape.message.UserInformationMessage;
import fr.lyrgard.hexScape.message.WarningMessage;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
@JsonSubTypes({
		@Type(value = ArmyLoadedMessage.class, name = "ArmyLoadedMessage"),
		@Type(value = ConnectedToServerMessage.class, name = "ConnectedToServerMessage"),
		@Type(value = CreateGameMessage.class, name = "CreateGameMessage"),
		@Type(value = ErrorMessage.class, name = "ErrorMessage"),
		@Type(value = DiceThrownMessage.class, name = "DiceThrownMessage"),
		@Type(value = DisconnectedFromServerMessage.class, name = "DisconnectedFromServerMessage"),
		@Type(value = GameCreatedMessage.class, name = "GameCreatedMessage"),
		@Type(value = GameEndedMessage.class, name = "GameEndedMessage"),
		@Type(value = GameJoinedMessage.class, name = "GameJoinedMessage"),
		@Type(value = GameLeftMessage.class, name = "GameLeftMessage"),
		@Type(value = GameMessagePostedMessage.class, name = "GameMessagePostedMessage"),
		@Type(value = GameObservedMessage.class, name = "GameObservedMessage"),
		@Type(value = GameStartedMessage.class, name = "GameStartedMessage"),
		@Type(value = HeartBeatMessage.class, name = "HeartBeatMessage"),
		@Type(value = InfoMessage.class, name = "InfoMessage"),
		@Type(value = JoinGameMessage.class, name = "JoinGameMessage"),
		@Type(value = JoinRoomMessage.class, name = "JoinRoomMessage"),
		@Type(value = LeaveGameMessage.class, name = "LeaveGameMessage"),
		@Type(value = LeaveRoomMessage.class, name = "LeaveRoomMessage"),
		@Type(value = MarkerPlacedMessage.class, name = "MarkerPlacedMessage"),
		@Type(value = MarkerRemovedMessage.class, name = "MarkerRemovedMessage"),
		@Type(value = MarkerRevealedMessage.class, name = "MarkerRevealedMessage"),
		@Type(value = RestoreGameMessage.class, name = "RestoreGameMessage"),
	    @Type(value = RoomMessagePostedMessage.class, name = "MessagePostedMessage"),
	    @Type(value = PlaceMarkerMessage.class, name = "PlaceMarkerMessage"),
	    @Type(value = UserJoinedRoomMessage.class, name = "PlayerJoinedRoomMessage"),
	    @Type(value = ObserveGameMessage.class, name = "ObserveGameMessage"),
	    @Type(value = PieceMovedMessage.class, name = "PieceMovedMessage"),
	    @Type(value = PiecePlacedMessage.class, name = "PiecePlacedMessage"),
	    @Type(value = PieceRemovedMessage.class, name = "PieceRemovedMessage"),
	    @Type(value = PieceSecondarySelectedMessage.class, name = "PieceSecondarySelectedMessage"),
	    @Type(value = PieceSecondaryUnselectedMessage.class, name = "PieceSecondaryUnselectedMessage"),
	    @Type(value = PieceSelectedMessage.class, name = "PieceSelectedMessage"),
	    @Type(value = PieceUnselectedMessage.class, name = "PieceUnselectedMessage"),
	    @Type(value = PostGameMessageMessage.class, name = "PostGameMessageMessage"),
	    @Type(value = PostRoomMessageMessage.class, name = "PostMessageMessage"),
	    @Type(value = RemoveMarkerMessage.class, name = "RemoveMarkerMessage"),
	    @Type(value = RevealMarkerMessage.class, name = "RevealMarkerMessage"),
	    @Type(value = RoomJoinedMessage.class, name = "RoomJoinedMessage"),
	    @Type(value = RoomLeftMessage.class, name = "RoomLeftMessage"),
	    @Type(value = StartGameMessage.class, name = "StartGameMessage"),
	    @Type(value = ThrowDiceMessage.class, name = "ThrowDiceMessage"),
	    @Type(value = UserIdAllocatedMessage.class, name = "UserIdAllocatedMessage"),
	    @Type(value = UserInformationMessage.class, name = "UserInformationMessage"),
	    @Type(value = WarningMessage.class, name = "WarningMessage")
	    }) 
public interface PolymorphicMessageMixIn {

}
