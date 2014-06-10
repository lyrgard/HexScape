package fr.lyrgard.hexScape.server.service;

import fr.lyrgard.hexScape.model.game.Game;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.model.room.Room;
import fr.lyrgard.hexScape.server.HexScapeServer;

public class RoomService {

//	public void sendRoomToPlayer(String roomId, Player player) {
//		HostedConnection client = HexScapeServer.getInstance().getConnectionByPlayerIds().get(player.getId());
//		client.send(getRoomContentMessage(roomId));
//	}
//	
//	private RoomContentResponseMessage getRoomContentMessage(String roomId) {
//		Room room = HexScapeServer.getInstance().getRooms().get(roomId);
//		Room resultRoom = null;
//		if (room != null) {
//			resultRoom = new Room();
//			resultRoom.setName(room.getName());
//			for (Player player : room.getPlayers()) {
//				Player resultPlayer = convert(player);
//				resultRoom.getPlayers().add(resultPlayer);
//			}
//			for (Game game : room.getGames()) {
//				Game resultGame = new Game();
//				for (Player player : game.getPlayers()) {
//					Player resultPlayer = convert(player);
//					resultGame.getPlayers().add(resultPlayer);
//				}
//				resultRoom.getGames().add(game);
//			}
//		}
//		RoomContentResponseMessage message = new RoomContentResponseMessage();
//		message.setRoom(resultRoom);
//		return message;
//	}
//	
//	private Player convert(Player player) {
//		Player resultPlayer = new Player();
//		resultPlayer.setId(player.getId());
//		resultPlayer.setName(player.getName());
//		resultPlayer.setColor(player.getColor());
//		return resultPlayer;
//	}
//	
//	public void broadcastRoomMessage(String roomId, Player player, String message) {
//		Room room = HexScapeServer.getInstance().getRooms().get(roomId);
//		if (room != null) {
//			RoomMessagePostedMessage m = new RoomMessagePostedMessage();
//			m.setMessage(message);
//			m.setPlayer(convert(player));
//			m.setRoomId(roomId);
//			
//			for (Player playerInRoom : room.getPlayers()) {
//				HostedConnection client = HexScapeServer.getInstance().getConnectionByPlayerIds().get(playerInRoom.getId());
//				client.send(m);
//			}
//		}
//	}
//	
//	public void joinRoom(String roomId, Player player) {
//		if (player.getRoom() != null) {
//			leaveRoom(player);
//		}
//		Room room = HexScapeServer.getInstance().getRooms().get(roomId);
//		if (room != null) {
//			room.getPlayers().add(player);
//			player.setRoom(room);
//			
//			PlayerJoinedRoomMessage message = new PlayerJoinedRoomMessage();
//			message.setPlayer(convert(player));
//			message.setRoomId(roomId);
//			
//			for (Player playerInRoom : room.getPlayers()) {
//				if (!playerInRoom.getId().equals(player.getId())) {
//					HostedConnection client = HexScapeServer.getInstance().getConnectionByPlayerIds().get(playerInRoom.getId());
//					client.send(message);
//				}
//			}
//		}
//		
//	}
//	
//	public void leaveRoom(Player player) {
//		Room room = player.getRoom();
//		if (room != null) {
//			PlayerLeftRoomMessage message = new PlayerLeftRoomMessage();
//			message.setPlayer(convert(player));
//			message.setRoomId(room.getId());
//			
//			for (Player playerInRoom : room.getPlayers()) {
//				if (!playerInRoom.getId().equals(player.getId())) {
//					HostedConnection client = HexScapeServer.getInstance().getConnectionByPlayerIds().get(playerInRoom.getId());
//					client.send(message);
//				}
//			}
//			room.getPlayers().remove(player);
//			player.setRoom(null);
//		}
//	}
}
