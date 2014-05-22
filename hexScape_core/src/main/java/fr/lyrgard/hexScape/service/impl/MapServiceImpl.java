package fr.lyrgard.hexScape.service.impl;

import java.io.File;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.control.PieceControlerAppState;
import fr.lyrgard.hexScape.event.MapLoadedEvent;
import fr.lyrgard.hexScape.event.WarningEvent;
import fr.lyrgard.hexScape.io.virtualScape.VirtualScapeMapReader;
import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.Scene;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.service.MapService;

public class MapServiceImpl implements MapService {

	private VirtualScapeMapReader mapReader = new VirtualScapeMapReader();
	
	@Override
	public void loadMap(File file) {
		HexScapeCore.getInstance().getHexScapeJme3Application().setScene(null);
		
		Map map = mapReader.readMap(file);
		Scene scene = new Scene();
		scene.setMap(map);
		
		HexScapeCore.getInstance().getHexScapeJme3Application().setScene(scene);
		HexScapeCore.getInstance().getEventBus().post(new MapLoadedEvent(map));
	}

	@Override
	public void placePiece(MoveablePiece piece) {
		if (HexScapeCore.getInstance().getHexScapeJme3Application().getScene() == null) {
			HexScapeCore.getInstance().getEventBus().post(new WarningEvent("No map was loaded. Please load a map before trying to place pieces"));
			return;
		}
		PieceControlerAppState pieceController = HexScapeCore.getInstance().getHexScapeJme3Application().getPieceControlerAppState();
		pieceController.addPiece(piece);
	}

	@Override
	public boolean placePiece(MoveablePiece piece, int x, int y, int z) {
		Scene scene = HexScapeCore.getInstance().getHexScapeJme3Application().getScene();
		if (scene == null) {
			HexScapeCore.getInstance().getEventBus().post(new WarningEvent("No map was loaded. Please load a map before trying to place pieces"));
			return false;
		}
		
		Tile nearestTile = getCurrentMap().getNearestTile(x, y, z);
		
		
		MoveablePiece alreadyTherePiece = scene.getPiece(nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());
		if (alreadyTherePiece != null && alreadyTherePiece != piece) {
			// already another piece here !
			return false;
		}
		
		if (scene.contains(piece)) {
			scene.removePiece(piece);
		}
		HexScapeCore.getInstance().getHexScapeJme3Application().getScene().addPiece(piece, nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());
		return true;
	}

	@Override
	public Map getCurrentMap() {
		return HexScapeCore.getInstance().getHexScapeJme3Application().getScene().getMap();
	}

	@Override
	public void moveSelectedPiece() {
		if (HexScapeCore.getInstance().getHexScapeJme3Application().getScene() == null) {
			HexScapeCore.getInstance().getEventBus().post(new WarningEvent("No map was loaded. Please load a map before trying to move a piece"));
			return;
		}
		PieceControlerAppState pieceController = HexScapeCore.getInstance().getHexScapeJme3Application().getPieceControlerAppState();
		pieceController.moveSelectedPiece();
	}

	@Override
	public void removePiece(MoveablePiece piece) {
		Scene scene = HexScapeCore.getInstance().getHexScapeJme3Application().getScene();
		if (scene.contains(piece)) {
			scene.removePiece(piece);
		}
	}

}
