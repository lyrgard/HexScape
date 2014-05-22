package fr.lyrgard.hexScape.service;

import java.io.File;

import fr.lyrgard.hexScape.model.MoveablePiece;
import fr.lyrgard.hexScape.model.map.Map;

public interface MapService {

	void loadMap(File file);
	
	Map getCurrentMap();
	
	void placePiece(MoveablePiece piece);
	
	boolean placePiece(MoveablePiece piece, int x, int y, int z);
	
	void moveSelectedPiece();
	
	void removePiece(MoveablePiece piece);
}
